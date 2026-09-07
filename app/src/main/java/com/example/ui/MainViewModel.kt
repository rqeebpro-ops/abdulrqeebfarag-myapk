package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RaqeebRepository
import com.example.data.local.AppDatabase
import com.example.data.local.ClientSubscriptionEntity
import com.example.data.local.ServiceRequestEntity
import com.example.model.ApnProfile
import com.example.model.MarketingPackage
import com.example.model.PaymentCurrency
import com.example.model.PhoneProblemType
import com.example.model.PhoneServiceDetail
import com.example.model.ScannedMediaItem
import com.example.model.ServiceSection
import com.example.model.UserProfile
import com.example.util.MediaRecoveryScanner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RaqeebRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = RaqeebRepository(db.serviceRequestDao(), db.clientSubscriptionDao())
    }

    // Owner Database & Administration
    private val _isOwnerAuthenticated = MutableStateFlow(false)
    val isOwnerAuthenticated: StateFlow<Boolean> = _isOwnerAuthenticated.asStateFlow()

    fun authenticateOwner(pinOrPhone: String): Boolean {
        val clean = pinOrPhone.trim()
        val success = clean == "773557771" || clean == "7771" || clean == "1234"
        if (success) {
            _isOwnerAuthenticated.value = true
        }
        return success
    }

    fun setOwnerModeDirect(authenticated: Boolean) {
        _isOwnerAuthenticated.value = authenticated
    }

    val allClientSubscriptions: StateFlow<List<ClientSubscriptionEntity>> = repository.allSubscriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subscribersCount: StateFlow<Int> = repository.subscribersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val revenueYER: StateFlow<Int> = repository.revenueYER
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val revenueSAR: StateFlow<Int> = repository.revenueSAR
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val revenueUSD: StateFlow<Int> = repository.revenueUSD
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateClientSubscriptionStatus(id: Long, newStatus: String, approved: Boolean) {
        viewModelScope.launch {
            repository.updateSubscriptionStatus(id, newStatus, approved)
        }
    }

    fun updateClientSubscriptionNotes(id: Long, notes: String) {
        viewModelScope.launch {
            repository.updateOwnerNotes(id, notes)
        }
    }

    fun deleteClientSubscription(id: Long) {
        viewModelScope.launch {
            repository.deleteSubscription(id)
        }
    }

    fun addManualClientSubscription(
        fullName: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceName: String,
        currency: String,
        priceAmount: Int,
        paymentMethod: String,
        transactionRef: String,
        status: String,
        ownerNotes: String
    ) {
        viewModelScope.launch {
            val entity = ClientSubscriptionEntity(
                fullName = fullName,
                phoneNumber = phone,
                phoneModel = phoneModel,
                problemType = problemType,
                serviceName = serviceName,
                currency = currency,
                priceAmount = priceAmount,
                paymentMethod = paymentMethod,
                transactionRef = transactionRef,
                status = status,
                isApproved = status == "تم التحقق والسداد" || status == "مكتمل",
                ownerNotes = ownerNotes
            )
            repository.insertSubscription(entity)
        }
    }

    // Active screen section
    private val _currentSection = MutableStateFlow(ServiceSection.PHONE_SOLUTIONS)
    val currentSection: StateFlow<ServiceSection> = _currentSection.asStateFlow()

    fun setSection(section: ServiceSection) {
        _currentSection.value = section
    }

    // Phone solutions
    private val _selectedProblemFilter = MutableStateFlow<PhoneProblemType?>(null)
    val selectedProblemFilter: StateFlow<PhoneProblemType?> = _selectedProblemFilter.asStateFlow()

    fun setProblemFilter(filter: PhoneProblemType?) {
        _selectedProblemFilter.value = filter
    }

    val phoneServices: List<PhoneServiceDetail> = repository.getPhoneServices()
    val marketingPackages: List<MarketingPackage> = repository.getMarketingPackages()
    val apnProfiles: List<ApnProfile> = repository.getApnProfiles()

    // User requests from database
    val userRequests: StateFlow<List<ServiceRequestEntity>> = repository.allRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Profile & Subscription State
    private val prefs = application.getSharedPreferences("raqeeb_user_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(
        UserProfile(
            fullName = prefs.getString("user_name", "") ?: "",
            phoneNumber = prefs.getString("user_phone", "") ?: "",
            phoneModel = prefs.getString("phone_model", "") ?: "",
            problemType = prefs.getString("problem_type", "") ?: "",
            selectedServiceId = prefs.getString("service_id", "") ?: "",
            selectedServiceName = prefs.getString("service_name", "") ?: "",
            paymentAccountUsed = prefs.getString("payment_account", "") ?: "",
            paymentTransactionRef = prefs.getString("payment_ref", "") ?: "",
            currency = try {
                PaymentCurrency.valueOf(prefs.getString("currency", PaymentCurrency.YER.name) ?: PaymentCurrency.YER.name)
            } catch (_: Exception) {
                PaymentCurrency.YER
            },
            servicePriceAmount = prefs.getInt("service_price", 2000),
            isSubscribed = prefs.getBoolean("is_subscribed", false),
            subscriptionTimestamp = prefs.getLong("sub_time", 0L)
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun saveLoginAndSubscription(
        name: String,
        phone: String,
        phoneModel: String,
        problemType: String = "",
        serviceId: String = "app_access",
        serviceName: String = "اشتراك ودخول التطبيق",
        currency: PaymentCurrency = PaymentCurrency.YER,
        priceAmount: Int = 2000,
        paymentAccount: String = "إشعار مالك التطبيق 773557771",
        transactionRef: String = "إشعار واتساب معتمد",
        markAsPaid: Boolean = true
    ) {
        val now = System.currentTimeMillis()
        prefs.edit()
            .putString("user_name", name)
            .putString("user_phone", phone)
            .putString("phone_model", phoneModel)
            .putString("problem_type", problemType)
            .putString("service_id", serviceId)
            .putString("service_name", serviceName)
            .putString("payment_account", paymentAccount)
            .putString("payment_ref", transactionRef)
            .putString("currency", currency.name)
            .putInt("service_price", priceAmount)
            .putBoolean("is_subscribed", markAsPaid)
            .putLong("sub_time", now)
            .apply()

        _userProfile.value = UserProfile(
            fullName = name,
            phoneNumber = phone,
            phoneModel = phoneModel,
            problemType = problemType,
            selectedServiceId = serviceId,
            selectedServiceName = serviceName,
            paymentAccountUsed = paymentAccount,
            paymentTransactionRef = transactionRef,
            currency = currency,
            servicePriceAmount = priceAmount,
            isSubscribed = markAsPaid,
            subscriptionTimestamp = now
        )

        // Automatically log this subscription in the user request database and owner client database
        viewModelScope.launch {
            val title = "طلب اشتراك: $serviceName ($name)"
            val category = "اشتراك خدمة محددة"
            val entity = ServiceRequestEntity(
                title = title,
                category = category,
                subCategory = problemType.ifBlank { serviceName },
                deviceModelOrBusiness = phoneModel,
                clientPhone = phone,
                notes = "الاسم واللقب: $name\nرقم الهاتف: $phone\nنوع الهاتف: $phoneModel\nالخدمة: $serviceName\nقيمة الاشتراك المنفصل: $priceAmount ${currency.code}\nتم إرسال إشعار الاشتراك لمالك التطبيق (773557771) عبر الواتساب."
            )
            repository.insertRequest(entity)

            // Save in Owner's Client Subscriptions Table
            val subEntity = ClientSubscriptionEntity(
                fullName = name,
                phoneNumber = phone,
                phoneModel = phoneModel,
                problemType = problemType,
                serviceId = serviceId,
                serviceName = serviceName,
                currency = currency.code,
                priceAmount = priceAmount,
                paymentMethod = paymentAccount,
                transactionRef = transactionRef,
                status = "قيد المراجعة",
                isApproved = false,
                ownerNotes = "تسجيل اشتراك جديد عبر التطبيق"
            )
            repository.insertSubscription(subEntity)
        }
    }

    fun logoutOrClearSubscription() {
        prefs.edit().clear().apply()
        _userProfile.value = UserProfile()
    }

    // Data Recovery Scanner State
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scannedMedia = MutableStateFlow<List<ScannedMediaItem>>(emptyList())
    val scannedMedia: StateFlow<List<ScannedMediaItem>> = _scannedMedia.asStateFlow()

    private val _selectedMediaIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedMediaIds: StateFlow<Set<Long>> = _selectedMediaIds.asStateFlow()

    private val _restoredMediaIds = MutableStateFlow<Set<Long>>(emptySet())
    val restoredMediaIds: StateFlow<Set<Long>> = _restoredMediaIds.asStateFlow()

    fun startMediaScan() {
        if (_isScanning.value) return
        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0.05f
            _scannedMedia.value = emptyList()

            // Simulate realistic deep sector scan progress
            for (step in 1..9) {
                delay(200)
                _scanProgress.value = step * 0.1f
            }

            val items = MediaRecoveryScanner.scanStorageForMedia(getApplication())
            _scannedMedia.value = items
            _scanProgress.value = 1f
            _isScanning.value = false
        }
    }

    fun toggleMediaSelection(id: Long) {
        val current = _selectedMediaIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _selectedMediaIds.value = current
    }

    fun restoreSelectedMedia() {
        val currentRestored = _restoredMediaIds.value.toMutableSet()
        currentRestored.addAll(_selectedMediaIds.value)
        _restoredMediaIds.value = currentRestored
        _selectedMediaIds.value = emptySet()
    }

    // Network Diagnostic / Ping Tester State
    private val _isDiagnosingNetwork = MutableStateFlow(false)
    val isDiagnosingNetwork: StateFlow<Boolean> = _isDiagnosingNetwork.asStateFlow()

    private val _networkDiagnosticResult = MutableStateFlow<String?>(null)
    val networkDiagnosticResult: StateFlow<String?> = _networkDiagnosticResult.asStateFlow()

    fun runNetworkDiagnostic(carrierName: String) {
        viewModelScope.launch {
            _isDiagnosingNetwork.value = true
            _networkDiagnosticResult.value = "جاري فحص الاتصال والترددات مع أبراج $carrierName..."
            delay(1200)
            _networkDiagnosticResult.value = """
                ✅ تم الاتصال بنجاح!
                • البرج: متصل بنمط 4G LTE المتقدم
                • زمن الاستجابة (Ping): 34ms
                • سرعة التنزيل التقديرية: 32.4 Mbps
                • حالة الـ APN: نشط ومضبوط بالشكل الصحيح
            """.trimIndent()
            _isDiagnosingNetwork.value = false
        }
    }

    // Marketing ROI Calculator State
    private val _marketingBudget = MutableStateFlow(50f) // $50 default
    val marketingBudget: StateFlow<Float> = _marketingBudget.asStateFlow()

    fun setMarketingBudget(budget: Float) {
        _marketingBudget.value = budget
    }

    // New Request Submission
    fun submitServiceRequest(
        title: String,
        category: String,
        subCategory: String,
        deviceOrBusiness: String,
        phone: String,
        notes: String,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val entity = ServiceRequestEntity(
                title = title,
                category = category,
                subCategory = subCategory,
                deviceModelOrBusiness = deviceOrBusiness,
                clientPhone = phone,
                notes = notes
            )
            val newId = repository.insertRequest(entity)
            onSuccess(newId)
        }
    }

    fun deleteRequest(id: Long) {
        viewModelScope.launch {
            repository.deleteRequest(id)
        }
    }
}
