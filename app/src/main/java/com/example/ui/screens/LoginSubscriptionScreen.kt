package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PaymentChannel
import com.example.model.PaymentCurrency
import com.example.model.PhoneServiceDetail
import com.example.model.ServicePricing
import com.example.model.UserProfile
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.AccentTealContainer
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LoginSubscriptionScreen(
    userProfile: UserProfile,
    services: List<PhoneServiceDetail>,
    isGateMode: Boolean = false,
    onSaveSubscription: (
        name: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceId: String,
        serviceName: String,
        currency: PaymentCurrency,
        priceAmount: Int,
        paymentAccount: String,
        transactionRef: String
    ) -> Unit,
    onLogout: () -> Unit,
    onProceedToServices: () -> Unit,
    onOpenOwnerDatabase: (() -> Unit)? = null
) {
    val context = LocalContext.current

    // Form inputs state as requested: الاسم واللقب، رقم الهاتف، نوع الهاتف، نوع المشكلة
    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var phoneNumber by remember { mutableStateOf(userProfile.phoneNumber) }
    var phoneModel by remember { mutableStateOf(userProfile.phoneModel) }
    var problemType by remember { mutableStateOf(userProfile.problemType) }

    // Service selection
    var selectedService by remember {
        mutableStateOf(
            services.firstOrNull { it.id == userProfile.selectedServiceId } ?: services.firstOrNull()
        )
    }
    var isServiceDropdownExpanded by remember { mutableStateOf(false) }

    // Currency selection (YER / SAR / USD)
    var selectedCurrency by remember { mutableStateOf(userProfile.currency) }

    // Current service pricing
    val currentPricing = selectedService?.pricing ?: ServicePricing(yer = 2000, sar = 15, usd = 4)
    val currentAmount = currentPricing.getAmount(selectedCurrency)

    // Payment Channel selection
    val paymentChannels = remember {
        listOf(
            PaymentChannel(
                id = "kuraimi",
                name = "بنك الكريمي",
                accountOrPhone = AppIntentUtils.KURAMI_ACCOUNT,
                typeLabel = "رقم الحساب",
                badge = "حساب بنكي مميز",
                instruction = "التحويل عبر تطبيق الكريمي جوال أو أقرب فرع/وكيل كريمي إكسبرس"
            ),
            PaymentChannel(
                id = "jeep",
                name = "محفظة جيب (Jeep)",
                accountOrPhone = AppIntentUtils.JEEP_WALLET_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "محفظة كاك بنك",
                instruction = "التحويل المباشر من محفظة جيب إلى رقم الحساب 782916997"
            ),
            PaymentChannel(
                id = "one_cash",
                name = "محفظة ون كاش (One Cash)",
                accountOrPhone = AppIntentUtils.ONE_CASH_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "محفظة إلكترونية",
                instruction = "التحويل الفوري من تطبيق ون كاش إلى الرقم 782916997"
            ),
            PaymentChannel(
                id = "jawwali",
                name = "محفظة جوالي (Jawwali)",
                accountOrPhone = AppIntentUtils.JAWWALI_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "محفظة بنك اليمن والكويت",
                instruction = "التحويل المباشر من تطبيق جوالي إلى الرقم 782916997"
            )
        )
    }

    var selectedPaymentChannel by remember {
        mutableStateOf(
            paymentChannels.firstOrNull { it.name in userProfile.paymentAccountUsed } ?: paymentChannels.first()
        )
    }
    var isWalletDropdownExpanded by remember { mutableStateOf(false) }

    var transactionRef by remember { mutableStateOf(userProfile.paymentTransactionRef) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var hasSentNotificationToOwner by remember { mutableStateOf(false) }
    var showSuccessCard by remember { mutableStateOf(userProfile.isSubscribed) }

    fun getWalletIcon(channelId: String): ImageVector {
        return when (channelId) {
            "kuraimi" -> Icons.Default.AccountBalance
            "jeep" -> Icons.Default.AccountBalanceWallet
            "one_cash" -> Icons.Default.CreditCard
            "jawwali" -> Icons.Default.PhoneAndroid
            else -> Icons.Default.AccountBalanceWallet
        }
    }

    fun getWalletShortName(channelId: String): String {
        return when (channelId) {
            "kuraimi" -> "الكريمي"
            "jeep" -> "جيب"
            "one_cash" -> "ون كاش"
            "jawwali" -> "جوالي"
            else -> "محفظة"
        }
    }

    // Quick suggestion chips for Phone Brands
    val commonPhoneBrands = remember {
        listOf("سامسونج (Samsung)", "شاومي / ريدمي", "آيفون (Apple)", "هواوي / هونر", "ريلمي (Realme)", "تكنو / إنفينكس", "أوبو (Oppo)")
    }

    // Quick suggestion chips for Problem Types
    val commonProblems = remember {
        listOf(
            "نسيان رمز القفل / فك قفل الشاشة",
            "تجاوز حساب جوجل (FRP)",
            "ضبط تفعيل 3G/4G وشبكة الهاتف",
            "استعادة صور وملفات محذوفة",
            "بطء وتعليق الهاتف وفورمات",
            "تفليش روم وتحديث النظام",
            "تسويق رقمي وإعلانات ممولة"
        )
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = EmeraldPrimary,
        unfocusedBorderColor = ImmersiveBorder,
        focusedLabelColor = EmeraldPrimary,
        unfocusedLabelColor = TextSlate,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedPlaceholderColor = TextMuted,
        unfocusedPlaceholderColor = TextMuted,
        cursorColor = EmeraldPrimary,
        focusedContainerColor = ImmersiveSurfaceVariant,
        unfocusedContainerColor = ImmersiveSurfaceVariant
    )

    // Neutral colors for Electronic Wallets section (Strictly NO green text/highlights)
    val walletTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AccentBlue,
        unfocusedBorderColor = ImmersiveBorder,
        focusedLabelColor = AccentBlue,
        unfocusedLabelColor = TextSlate,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedPlaceholderColor = TextMuted,
        unfocusedPlaceholderColor = TextMuted,
        cursorColor = AccentBlue,
        focusedContainerColor = ImmersiveSurfaceVariant,
        unfocusedContainerColor = ImmersiveSurfaceVariant
    )

    fun buildOwnerNotificationMessage(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
        val nowFormatted = dateFormat.format(Date())
        val serviceTitle = selectedService?.title ?: "خدمة برمجية وتقنية"
        val pricePricing = selectedService?.pricing ?: ServicePricing(2000, 15, 4)
        val selectedAmt = pricePricing.getAmount(selectedCurrency)
        return """
            🔔 إشعار طلب اشتراك ودخول للتطبيق 🔔
            مرسل إلى: مالك تطبيق مركز رقيب (773557771)
            ━━━━━━━━━━━━━━━━━━━━
            👤 الاسم واللقب: ${fullName.trim()}
            📱 رقم الهاتف: ${phoneNumber.trim()}
            📲 نوع الهاتف: ${phoneModel.trim()}
            ⚠️ نوع المشكلة: ${problemType.trim()}
            ━━━━━━━━━━━━━━━━━━━━
            🛠️ الخدمة المختارة (اشتراك منفصل): $serviceTitle
            💰 قيمة اشتراك الخدمة: $selectedAmt ${selectedCurrency.code} (${selectedCurrency.symbolAr})
            📊 التسعيرة بجميع العملات:
               • ريال يمني: ${pricePricing.yer} YER
               • ريال سعودي: ${pricePricing.sar} SAR
               • دولار أمريكي: ${pricePricing.usd} USD
            💵 العملة المحددة للدفع: ${selectedCurrency.code} (${selectedCurrency.symbolAr})
            💳 الحساب المحول إليه: ${selectedPaymentChannel.name} (${selectedPaymentChannel.accountOrPhone})
            ${if (transactionRef.isNotBlank()) "🔖 رقم السند / العملية: ${transactionRef.trim()}" else "🔖 حالة السداد: إشعار تحويل مباشر"}
            ⏰ وقت وتاريخ الإرسال: $nowFormatted
            ━━━━━━━━━━━━━━━━━━━━
            أرجو قبول طلبي وتأكيد اشتراكي للدخول إلى خدمات التطبيق.
        """.trimIndent()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, EmeraldContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkButtonBg)
                            .border(1.dp, DarkButtonBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.raqeeb_icon_1788464615035),
                            contentDescription = "شعار رقيب",
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isGateMode) "بوابة تسجيل الدخول والاشتراك" else "تسجيل الدخول والاشتراك بالخدمة",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "للدخول إلى خدمات التطبيق، يرجى تعبئة بياناتك وإرسال إشعار الاشتراك للمالك على 773557771",
                            fontSize = 12.sp,
                            color = EmeraldLight,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        // Owner Direct Access Banner (for app owner)
        if (onOpenOwnerDatabase != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkButtonBg),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenOwnerDatabase() }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "👑 أنا مالك التطبيق (م. عبد الرقيب فرج)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "الدخول المباشر لقاعدة بيانات المشتركين والإيرادات",
                                    fontSize = 11.sp,
                                    color = EmeraldLight
                                )
                            }
                        }
                        Button(
                            onClick = { onOpenOwnerDatabase() },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("دخول المالك", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Active Subscription Status Banner (if already subscribed and logged in)
        if (showSuccessCard || userProfile.isSubscribed) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0x1F34D399),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    text = "الحساب مسجل والاشتراك مفعّل بنجاح!",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Text(
                                    text = "مرحباً بك: ${fullName.ifBlank { userProfile.fullName }}",
                                    fontSize = 12.sp,
                                    color = TextWhite
                                )
                            }
                        }

                        // Subscription Details Summary
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ImmersiveSurfaceCard)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "• الاسم واللقب: ${fullName.ifBlank { userProfile.fullName }}",
                                fontSize = 12.sp,
                                color = TextWhite
                            )
                            Text(
                                text = "• رقم الهاتف: ${phoneNumber.ifBlank { userProfile.phoneNumber }}",
                                fontSize = 12.sp,
                                color = TextSlate
                            )
                            Text(
                                text = "• نوع الهاتف: ${phoneModel.ifBlank { userProfile.phoneModel }}",
                                fontSize = 12.sp,
                                color = TextSlate
                            )
                            if (problemType.isNotBlank() || userProfile.problemType.isNotBlank()) {
                                Text(
                                    text = "• نوع المشكلة: ${problemType.ifBlank { userProfile.problemType }}",
                                    fontSize = 12.sp,
                                    color = AccentOrange
                                )
                            }
                            Text(
                                text = "• الخدمة المختارة: ${selectedService?.title ?: userProfile.selectedServiceName.ifBlank { "خدمة برمجية" }}",
                                fontSize = 12.sp,
                                color = TextWhite
                            )
                            Text(
                                text = "• قيمة الاشتراك المنفصل: $currentAmount ${selectedCurrency.code} (${selectedCurrency.symbolAr})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "• وسيلة السداد: ${selectedPaymentChannel.name}",
                                fontSize = 12.sp,
                                color = AccentTeal
                            )
                            Text(
                                text = "• إشعار المالك: تم الإرسال لواتساب المالك 773557771",
                                fontSize = 11.sp,
                                color = EmeraldPrimary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onProceedToServices,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = ImmersiveBg
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("الدخول لخدمات التطبيق", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    onLogout()
                                    showSuccessCard = false
                                    hasSentNotificationToOwner = false
                                    fullName = ""
                                    phoneNumber = ""
                                    phoneModel = ""
                                    problemType = ""
                                    transactionRef = ""
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DarkButtonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = TextSlate
                                )
                            ) {
                                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تسجيل الخروج", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Section 1: Required Form Fields (الاسم واللقب، رقم الهاتف، نوع الهاتف، نوع المشكله)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = "1. بيانات تسجيل الدخول والاشتراك",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    // 1. الاسم واللقب
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            validationError = null
                        },
                        label = { Text("الاسم واللقب *") },
                        placeholder = { Text("مثال: عبد الرقيب فرج") },
                        leadingIcon = {
                            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = AccentTeal)
                        },
                        singleLine = true,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 2. رقم الهاتف
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            validationError = null
                        },
                        label = { Text("رقم الهاتف (واتساب) *") },
                        placeholder = { Text("مثال: 782916997") },
                        leadingIcon = {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = EmeraldPrimary)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 3. نوع الهاتف
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = phoneModel,
                            onValueChange = {
                                phoneModel = it
                                validationError = null
                            },
                            label = { Text("نوع وموديل الهاتف *") },
                            placeholder = { Text("مثال: سامسونج جالكسي A54 أو ريدمي نوت 12") },
                            leadingIcon = {
                                Icon(Icons.Default.Smartphone, contentDescription = null, tint = AccentOrange)
                            },
                            singleLine = true,
                            colors = textFieldColors,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "اختيار سريع لماركة الهاتف:",
                            fontSize = 11.sp,
                            color = TextSlate
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            commonPhoneBrands.forEach { brand ->
                                val isSelected = phoneModel.contains(brand.split(" ").first())
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) EmeraldContainer else DarkButtonBg,
                                    border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else DarkButtonBorder),
                                    modifier = Modifier.clickable {
                                        phoneModel = if (phoneModel.isBlank()) brand else "$brand - $phoneModel"
                                        validationError = null
                                    }
                                ) {
                                    Text(
                                        text = brand,
                                        fontSize = 10.sp,
                                        color = if (isSelected) EmeraldPrimary else TextSlate,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 4. نوع المشكله
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = problemType,
                            onValueChange = {
                                problemType = it
                                validationError = null
                            },
                            label = { Text("نوع المشكلة التي تواجهها *") },
                            placeholder = { Text("مثال: نسيت رمز القفل أو مشكلة شبكة أو فورمات") },
                            leadingIcon = {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = AccentPink)
                            },
                            singleLine = false,
                            maxLines = 3,
                            colors = textFieldColors,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "اختر نوع المشكلة من المقترحات أو اكتبها أعلاه:",
                            fontSize = 11.sp,
                            color = TextSlate
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            commonProblems.forEach { problem ->
                                val isSelected = problemType == problem
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) EmeraldContainer else DarkButtonBg,
                                    border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else DarkButtonBorder),
                                    modifier = Modifier.clickable {
                                        problemType = problem
                                        validationError = null
                                    }
                                ) {
                                    Text(
                                        text = problem,
                                        fontSize = 10.sp,
                                        color = if (isSelected) EmeraldPrimary else TextWhite,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Service Selection & Individual Pricing (كل خدمة لها قيمة واشتراك منفصل)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentOrangeContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = "2. الخدمة المطلوبة ورسوم الاشتراك المنفصل",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    // Notice: Each service has separate pricing & subscription
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x1DF59E0B),
                        border = BorderStroke(1.dp, AccentOrange.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(20.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "كل خدمة لها قيمة واشتراك منفصل عن الأخرى",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "اختر الخدمة المطلوبة واطلع على قيمتها برمز YER أو SAR أو USD أدناه:",
                                    fontSize = 11.sp,
                                    color = TextSlate
                                )
                            }
                        }
                    }

                    // Service Dropdown Selector
                    ExposedDropdownMenuBox(
                        expanded = isServiceDropdownExpanded,
                        onExpandedChange = { isServiceDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedService?.title ?: "اختر الخدمة البرمجية",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("الخدمة البرمجية المحددة للاشتراك") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isServiceDropdownExpanded) },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isServiceDropdownExpanded,
                            onDismissRequest = { isServiceDropdownExpanded = false },
                            modifier = Modifier.background(ImmersiveSurfaceCard)
                        ) {
                            services.forEach { srv ->
                                val srvPricing = srv.pricing ?: ServicePricing(2000, 15, 4)
                                val srvAmt = srvPricing.getAmount(selectedCurrency)
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(srv.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text(srv.type.titleAr, color = TextSlate, fontSize = 11.sp)
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = EmeraldContainer
                                            ) {
                                                Text(
                                                    text = "$srvAmt ${selectedCurrency.code}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = EmeraldPrimary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedService = srv
                                        isServiceDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Price with Currency Selector
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ImmersiveSurfaceVariant)
                            .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(14.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "قيمة اشتراك الخدمة المختارة:",
                                    fontSize = 12.sp,
                                    color = TextSlate
                                )
                                Text(
                                    text = selectedService?.title ?: "خدمة برمجية",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = EmeraldContainer,
                                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "$currentAmount ${selectedCurrency.code}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // Currency Selector (YER / SAR / USD)
                        Text(
                            text = "اختر العملة المفضلة لديك (YER / SAR / USD):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSlate
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PaymentCurrency.values().forEach { cur ->
                                val isSelected = selectedCurrency == cur
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCurrency = cur },
                                    label = {
                                        Text(
                                            text = "${cur.code} - ${cur.symbolAr}",
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = if (isSelected) {
                                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                    } else null,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmeraldPrimary,
                                        selectedLabelColor = ImmersiveBg,
                                        selectedLeadingIconColor = ImmersiveBg,
                                        containerColor = DarkButtonBg,
                                        labelColor = TextWhite
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = DarkButtonBorder,
                                        selectedBorderColor = EmeraldPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Currency Equivalents Breakdown
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = ImmersiveSurfaceCard,
                            border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("اليمني (YER)", fontSize = 10.sp, color = TextSlate)
                                    Text("${currentPricing.yer} YER", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedCurrency == PaymentCurrency.YER) EmeraldPrimary else TextWhite)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ImmersiveBorder))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("السعودي (SAR)", fontSize = 10.sp, color = TextSlate)
                                    Text("${currentPricing.sar} SAR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedCurrency == PaymentCurrency.SAR) EmeraldPrimary else TextWhite)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(ImmersiveBorder))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("الأمريكي (USD)", fontSize = 10.sp, color = TextSlate)
                                    Text("${currentPricing.usd} USD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedCurrency == PaymentCurrency.USD) EmeraldPrimary else TextWhite)
                                }
                            }
                        }
                    }

                    // Comprehensive Pricing Guide Table (للتعرف على الأسعار برمز YER او SAR او USD)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ImmersiveSurfaceVariant)
                            .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = AccentTeal, modifier = Modifier.size(18.dp))
                            Text(
                                text = "دليل أسعار الخدمات برمز YER و SAR و USD:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }

                        Text(
                            text = "انقر على أي خدمة لتحديدها مباشرة للاشتراك:",
                            fontSize = 11.sp,
                            color = TextSlate
                        )

                        services.forEach { srv ->
                            val srvPricing = srv.pricing ?: ServicePricing(2000, 15, 4)
                            val isChosen = selectedService?.id == srv.id
                            val currentCurPrice = srvPricing.getAmount(selectedCurrency)

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isChosen) EmeraldContainer else ImmersiveSurfaceCard,
                                border = BorderStroke(
                                    1.dp,
                                    if (isChosen) EmeraldPrimary else ImmersiveBorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedService = srv
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (isChosen) EmeraldPrimary else DarkButtonBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isChosen) Icons.Default.Check else Icons.Default.Smartphone,
                                            contentDescription = null,
                                            tint = if (isChosen) ImmersiveBg else EmeraldPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = srv.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = "${srvPricing.yer} YER | ${srvPricing.sar} SAR | ${srvPricing.usd} USD",
                                            fontSize = 10.sp,
                                            color = if (isChosen) EmeraldPrimary else TextSlate
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isChosen) EmeraldPrimary else DarkButtonBg,
                                        border = BorderStroke(1.dp, if (isChosen) EmeraldPrimary else DarkButtonBorder)
                                    ) {
                                        Text(
                                            text = "$currentCurPrice ${selectedCurrency.code}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isChosen) ImmersiveBg else EmeraldPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Electronic Wallets & Transfer Channels (المحافظ الإلكترونية وطرق السداد المعتمدة)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = AccentBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "3. المحافظ الإلكترونية والحسابات المعتمدة",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Text(
                        text = "اختر المحفظة الإلكترونية من القائمة المنسدلة أو عبر النقر على أيقونات المحافظ أدناه:",
                        fontSize = 12.sp,
                        color = TextSlate
                    )

                    // 1. Quick Wallet Icon Selector Cards (أيقونات المحافظ الإلكترونية)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        paymentChannels.forEach { channel ->
                            val isSelected = selectedPaymentChannel.id == channel.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) ImmersiveSurfaceVariant else DarkButtonBg,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) AccentBlue else DarkButtonBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedPaymentChannel = channel }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) AccentBlueContainer else ImmersiveSurface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getWalletIcon(channel.id),
                                            contentDescription = channel.name,
                                            tint = if (isSelected) AccentBlue else TextSlate,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Text(
                                        text = getWalletShortName(channel.id),
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextWhite else TextSlate,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    // 2. Dropdown Menu for Wallets (القائمة المنسدلة للمحافظ الإلكترونية)
                    ExposedDropdownMenuBox(
                        expanded = isWalletDropdownExpanded,
                        onExpandedChange = { isWalletDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedPaymentChannel.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("المحفظة أو الحساب المختار", color = TextSlate) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getWalletIcon(selectedPaymentChannel.id),
                                    contentDescription = null,
                                    tint = AccentBlue
                                )
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isWalletDropdownExpanded)
                            },
                            colors = walletTextFieldColors,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isWalletDropdownExpanded,
                            onDismissRequest = { isWalletDropdownExpanded = false },
                            modifier = Modifier.background(ImmersiveSurfaceCard)
                        ) {
                            paymentChannels.forEach { channel ->
                                val isChosen = selectedPaymentChannel.id == channel.id
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            imageVector = getWalletIcon(channel.id),
                                            contentDescription = null,
                                            tint = if (isChosen) AccentBlue else TextSlate
                                        )
                                    },
                                    text = {
                                        Column {
                                            Text(
                                                text = channel.name,
                                                color = TextWhite,
                                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = "${channel.typeLabel}: ${channel.accountOrPhone}",
                                                color = TextSlate,
                                                fontSize = 11.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedPaymentChannel = channel
                                        isWalletDropdownExpanded = false
                                    },
                                    modifier = Modifier.background(
                                        if (isChosen) ImmersiveSurfaceVariant else Color.Transparent
                                    )
                                )
                            }
                        }
                    }

                    // 3. Selected Wallet Details Card (كرت بيانات المحفظة المحددة - بدون كتابة باللون الأخضر)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, AccentBlue.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(AccentBlueContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = getWalletIcon(selectedPaymentChannel.id),
                                            contentDescription = null,
                                            tint = AccentBlue,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = selectedPaymentChannel.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = selectedPaymentChannel.badge,
                                            fontSize = 11.sp,
                                            color = TextSlate
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        AppIntentUtils.copyToClipboard(
                                            context,
                                            selectedPaymentChannel.name,
                                            selectedPaymentChannel.accountOrPhone
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, DarkButtonBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = DarkButtonBg,
                                        contentColor = TextWhite
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = TextWhite
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("نسخ الرقم", fontSize = 11.sp, color = TextWhite)
                                }
                            }

                            // Account Number Box (bold white typography, clean slate label, no green text)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ImmersiveSurface,
                                border = BorderStroke(1.dp, ImmersiveBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${selectedPaymentChannel.typeLabel}:",
                                        fontSize = 12.sp,
                                        color = TextSlate
                                    )
                                    Text(
                                        text = selectedPaymentChannel.accountOrPhone,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhite,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            // Transfer instructions (clean slate text, no green)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = TextSlate,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = selectedPaymentChannel.instruction,
                                    fontSize = 11.sp,
                                    color = TextSlate,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Optional Transaction Ref (Neutral colors, no green)
                    OutlinedTextField(
                        value = transactionRef,
                        onValueChange = { transactionRef = it },
                        label = { Text("رقم سند الإيداع أو الحوالة (اختياري)", color = TextSlate) },
                        placeholder = { Text("مثال: رقم العملية 1048593 أو إشعار التحويل", color = TextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = AccentBlue)
                        },
                        singleLine = true,
                        colors = walletTextFieldColors,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Error Message Banner
        if (validationError != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0x26F87171),
                    border = BorderStroke(1.dp, Color(0xFFF87171)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF87171), modifier = Modifier.size(22.dp))
                        Text(
                            text = validationError ?: "",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFCA5A5),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Notification Sent Status Confirmation (when WhatsApp is opened to owner)
        if (hasSentNotificationToOwner) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x1F34D399),
                    border = BorderStroke(1.dp, EmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
                            Text(
                                text = "تم توجيه الإشعار لمالك التطبيق (773557771) عبر الواتساب",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        Text(
                            text = "تأكد من الضغط على زر الإرسال في تطبيق واتساب للرقم 773557771، ثم اضغط على زر الدخول أدناه للدخول إلى كافة خدمات التطبيق مباشرة.",
                            fontSize = 12.sp,
                            color = TextWhite,
                            lineHeight = 18.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val msg = buildOwnerNotificationMessage()
                                    AppIntentUtils.openWhatsAppToOwner(context, msg)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = EmeraldLight
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إعادة فتح واتساب", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val msg = buildOwnerNotificationMessage()
                                    AppIntentUtils.copyToClipboard(context, "إشعار اشتراك تطبيق رقيب", msg)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, DarkButtonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = TextWhite
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("نسخ الإشعار", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Submission & Verification Buttons
        // REQUIREMENT: "ثم يضغط على الاشتراك للدخول التطبيق ولايتم دخول التطبيق الا بارسال اشعار لمالك التطبيق عبر الواتساب على الرقم 773557771"
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Main WhatsApp Subscription & Notification Button
                Button(
                    onClick = {
                        // Strict validation of the 4 requested fields:
                        if (fullName.isBlank()) {
                            validationError = "يرجى كتابة الاسم واللقب"
                            return@Button
                        }
                        if (phoneNumber.isBlank()) {
                            validationError = "يرجى كتابة رقم الهاتف للتواصل"
                            return@Button
                        }
                        if (phoneNumber.trim().length < 6) {
                            validationError = "يرجى إدخال رقم هاتف صحيح"
                            return@Button
                        }
                        if (phoneModel.isBlank()) {
                            validationError = "يرجى تحديد نوع وموديل الهاتف"
                            return@Button
                        }
                        if (problemType.isBlank()) {
                            validationError = "يرجى تحديد أو كتابة نوع المشكلة"
                            return@Button
                        }

                        validationError = null
                        val notificationMsg = buildOwnerNotificationMessage()

                        // 1. Send WhatsApp notification to App Owner at 773557771
                        AppIntentUtils.openWhatsAppToOwner(context, notificationMsg)
                        hasSentNotificationToOwner = true
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 15.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "الاشتراك وإرسال إشعار للمالك (773557771)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Entry button: only accessible once notification has been dispatched to owner
                Button(
                    onClick = {
                        if (!hasSentNotificationToOwner) {
                            validationError = "تنبيه: لا يمكن الدخول إلى خدمات التطبيق إلا بعد إرسال إشعار لمالك التطبيق على الرقم 773557771 عبر الواتساب."
                            return@Button
                        }
                        if (fullName.isBlank() || phoneNumber.isBlank() || phoneModel.isBlank() || problemType.isBlank()) {
                            validationError = "يرجى إكمال جميع الحقول المطلوبة أولاً"
                            return@Button
                        }

                        val service = selectedService ?: services.first()
                        val servicePricing = service.pricing ?: ServicePricing(2000, 15, 4)
                        val priceAmt = servicePricing.getAmount(selectedCurrency)
                        onSaveSubscription(
                            fullName.trim(),
                            phoneNumber.trim(),
                            phoneModel.trim(),
                            problemType.trim(),
                            service.id,
                            service.title,
                            selectedCurrency,
                            priceAmt,
                            "${selectedPaymentChannel.name} (${selectedPaymentChannel.accountOrPhone})",
                            transactionRef.trim()
                        )
                        showSuccessCard = true
                        validationError = null
                        onProceedToServices()
                    },
                    enabled = hasSentNotificationToOwner,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasSentNotificationToOwner) AccentTeal else DarkButtonBg,
                        contentColor = if (hasSentNotificationToOwner) ImmersiveBg else TextMuted,
                        disabledContainerColor = DarkButtonBg,
                        disabledContentColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasSentNotificationToOwner) "تأكيد الإرسال والدخول لخدمات التطبيق" else "الدخول للتطبيق (يتطلب إرسال الإشعار أولاً)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Help disclaimer
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ImmersiveSurfaceCard,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AccentTeal, modifier = Modifier.size(16.dp))
                        Text(
                            text = "مالك التطبيق: مركز رقيب للبرمجة والتسويق - واتساب مباشر معتمد: 773557771",
                            fontSize = 11.sp,
                            color = TextSlate,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
