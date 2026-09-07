package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_subscriptions")
data class ClientSubscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phoneNumber: String,
    val phoneModel: String,
    val problemType: String,
    val serviceId: String = "",
    val serviceName: String,
    val currency: String = "YER", // YER, SAR, USD
    val priceAmount: Int = 2000,
    val paymentMethod: String = "",
    val transactionRef: String = "",
    val status: String = "قيد المراجعة", // "قيد المراجعة", "تم التحقق والسداد", "مكتمل", "ملغي"
    val isApproved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val ownerNotes: String = ""
)
