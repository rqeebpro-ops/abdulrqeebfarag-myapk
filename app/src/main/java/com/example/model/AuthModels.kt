package com.example.model

data class ServicePricing(
    val yer: Int,
    val sar: Int,
    val usd: Int
) {
    fun getAmount(currency: PaymentCurrency): Int = when (currency) {
        PaymentCurrency.YER -> yer
        PaymentCurrency.SAR -> sar
        PaymentCurrency.USD -> usd
    }

    fun getFormatted(currency: PaymentCurrency): String {
        return "${getAmount(currency)} ${currency.code}"
    }

    fun getFormattedWithSymbol(currency: PaymentCurrency): String {
        return "${getAmount(currency)} ${currency.code} (${currency.symbolAr})"
    }

    fun getAllCurrenciesDisplay(): String {
        return "$yer YER  |  $sar SAR  |  $usd USD"
    }
}

enum class PaymentCurrency(val code: String, val symbolAr: String, val label: String) {
    YER("YER", "ريال يمني", "YER (ريال يمني)"),
    SAR("SAR", "ريال سعودي", "SAR (ريال سعودي)"),
    USD("USD", "دولار أمريكي", "USD (دولار أمريكي)")
}

data class UserProfile(
    val fullName: String = "",
    val phoneNumber: String = "",
    val phoneModel: String = "",
    val problemType: String = "",
    val selectedServiceId: String = "",
    val selectedServiceName: String = "",
    val paymentAccountUsed: String = "",
    val paymentTransactionRef: String = "",
    val currency: PaymentCurrency = PaymentCurrency.YER,
    val servicePriceAmount: Int = 2000,
    val isSubscribed: Boolean = false,
    val subscriptionTimestamp: Long = 0L
)

data class PaymentChannel(
    val id: String,
    val name: String,
    val accountOrPhone: String,
    val typeLabel: String,
    val badge: String,
    val instruction: String
)
