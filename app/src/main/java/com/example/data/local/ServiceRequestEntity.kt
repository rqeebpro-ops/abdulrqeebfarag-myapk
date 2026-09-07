package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "صيانة هواتف" or "تسويق إلكتروني"
    val subCategory: String, // e.g. "فك شفرة", "تفعيل 3G", "حملة إعلانية"
    val deviceModelOrBusiness: String,
    val clientPhone: String,
    val notes: String,
    val status: String = "قيد المراجعة", // "قيد المراجعة", "جاري التنفيذ", "مكتمل"
    val timestamp: Long = System.currentTimeMillis()
)
