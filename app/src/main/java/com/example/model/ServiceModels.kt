package com.example.model

enum class ServiceSection {
    PHONE_SOLUTIONS,
    LOCK_RECOVERY_SIMULATOR,
    MARKETING,
    DATA_RECOVERY_SCANNER,
    APN_SETTINGS,
    MY_REQUESTS,
    CONTACT_US,
    LOGIN_SUBSCRIPTION,
    OWNER_DATABASE
}

enum class PhoneProblemType(val titleAr: String, val titleEn: String, val iconName: String) {
    DATA_RECOVERY(
        "إسترداد البيانات والملفات",
        "Data & Media Recovery",
        "restore_page"
    ),
    DOCUMENTS_RECOVERY(
        "استرجاع المستندات والوثائق",
        "Documents Recovery",
        "description"
    ),
    CARRIER_UNLOCK(
        "فك شفرات الهاتف",
        "Carrier & SIM Unlock",
        "lock_open"
    ),
    ACCOUNT_RECOVERY(
        "إسترجاع الحسابات المخترقة",
        "Hacked Accounts Recovery",
        "manage_accounts"
    ),
    FAILURE_LOCK_SIMULATOR(
        "محاكي الفشل والأقفال",
        "Failure & Lock Simulator",
        "phonelink_lock"
    ),
    LOCK_BYPASS(
        "فك إقفال الهاتف وFRP",
        "Screen Lock & FRP Bypass",
        "lock"
    ),
    ARABIZATION(
        "تعريب الهاتف واللغات",
        "System Arabization",
        "translate"
    ),
    DATA_ACTIVATION_3G_4G(
        "تفعيل بيانات 3G / 4G",
        "3G/4G Data Activation",
        "network_cell"
    )
}

data class PhoneServiceDetail(
    val id: String,
    val type: PhoneProblemType,
    val title: String,
    val description: String,
    val steps: List<String>,
    val pricing: ServicePricing = ServicePricing(yer = 2000, sar = 15, usd = 4),
    val quickActionCode: String? = null, // e.g. *#06#, *#*#4636#*#*
    val settingsAction: String? = null, // e.g. "android.settings.APN_SETTINGS"
    val warningNote: String? = null
)

data class MarketingPackage(
    val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val icon: String,
    val estimatedReach: String,
    val startingPrice: String,
    val pricing: ServicePricing = ServicePricing(yer = 5000, sar = 38, usd = 10),
    val features: List<String>,
    val popularTag: Boolean = false
)

data class ApnProfile(
    val id: String,
    val carrierName: String,
    val country: String,
    val networkType: String, // 3G, 4G, 5G
    val apnName: String,
    val apnValue: String,
    val dialNumber: String = "#777",
    val username: String = "",
    val password: String = "",
    val mmsc: String = "",
    val instructions: String
)

data class ScannedMediaItem(
    val id: Long,
    val name: String,
    val path: String,
    val sizeBytes: Long,
    val mimeType: String,
    val isRecovered: Boolean = false,
    val dateModified: Long = System.currentTimeMillis()
)
