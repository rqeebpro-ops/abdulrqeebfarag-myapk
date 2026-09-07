package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils

@Composable
fun RequestServiceDialog(
    initialCategory: String = "صيانة وبرمجة هواتف",
    initialSubCategory: String = "فك شفرات الهاتف",
    onDismiss: () -> Unit,
    onSubmit: (title: String, category: String, subCategory: String, device: String, phone: String, notes: String) -> Unit
) {
    val context = LocalContext.current
    var clientName by remember { mutableStateOf("") }
    var deviceOrBusiness by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedSubCategory by remember { mutableStateOf(initialSubCategory) }
    var isSubmitted by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = EmeraldPrimary,
        unfocusedBorderColor = ImmersiveBorder,
        focusedLabelColor = EmeraldPrimary,
        unfocusedLabelColor = TextSlate,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedPlaceholderColor = TextMuted,
        unfocusedPlaceholderColor = TextMuted,
        cursorColor = EmeraldPrimary
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ImmersiveSurfaceCard,
            border = BorderStroke(1.dp, ImmersiveBorder),
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSubmitted) "تم إرسال الطلب بنجاح" else "طلب خدمة من رقيب",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isSubmitted) EmeraldPrimary else TextWhite
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextMuted)
                    }
                }

                if (isSubmitted) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.height(64.dp).width(64.dp)
                        )
                        Text(
                            text = "شكراً لتواصلك مع رقيب للبرمجة والتسويق!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "تم حفظ طلبك بنجاح في سجل الطلبات. يمكنك أيضاً إرسال تفاصيل طلبك مباشرة عبر الواتساب لتسريع المعالجة الفورية.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSlate
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val msg = """
                                    مرحباً مركز رقيب، أود الاستفسار عن طلب خدمة:
                                    • الخدمة: $selectedSubCategory ($selectedCategory)
                                    • الموديل / النشاط: $deviceOrBusiness
                                    • رقم التواصل: $clientPhone
                                    • ملاحظات: $notes
                                """.trimIndent()
                                AppIntentUtils.openWhatsApp(context, msg)
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                contentColor = ImmersiveBg
                            )
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إرسال عبر الواتساب الآن", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextWhite
                            )
                        ) {
                            Text("حفظ وإغلاق")
                        }
                    }
                } else {
                    // Form
                    Text(
                        text = "الخدمة المطلوبة: $selectedSubCategory",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentTeal
                    )

                    // Subscription fee and payment info banner
                    val servicePricing = when {
                        selectedSubCategory.contains("بيانات", true) || selectedSubCategory.contains("ملفات", true) -> Triple("4000 YER", "30 SAR", "8 USD")
                        selectedSubCategory.contains("مستندات", true) || selectedSubCategory.contains("وثائق", true) -> Triple("3000 YER", "22 SAR", "6 USD")
                        selectedSubCategory.contains("شفرات", true) || selectedSubCategory.contains("تشفير", true) -> Triple("3000 YER", "22 SAR", "6 USD")
                        selectedSubCategory.contains("حسابات", true) || selectedSubCategory.contains("اختراق", true) -> Triple("3000 YER", "22 SAR", "6 USD")
                        selectedSubCategory.contains("فشل", true) || selectedSubCategory.contains("محاكي", true) -> Triple("7000 YER", "52 SAR", "14 USD")
                        else -> Triple("2000 YER", "15 SAR", "4 USD")
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ImmersiveSurfaceCard,
                        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Payment, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                    Text("قيمة اشتراك هذه الخدمة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                                Text(servicePricing.first, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                            }
                            Text(
                                text = "ما يقابلها بالعملات: (${servicePricing.second} / ${servicePricing.third}) - اشتراك منفصل",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSlate
                            )
                            Text(
                                text = "الحسابات المعتمدة: الكريمي (3001845477) | جيب / ون كاش / جوالي (782916997)",
                                fontSize = 10.sp,
                                color = TextMuted,
                                lineHeight = 14.sp
                            )
                        }
                    }

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("الاسم الكامل للعميل") },
                        placeholder = { Text("مثال: عبد الرقيب فرج") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("رقم هاتفك للتواصل (واتساب)") },
                        placeholder = { Text("مثال: 782916997") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = deviceOrBusiness,
                        onValueChange = { deviceOrBusiness = it },
                        label = { Text("نوع الهاتف وموديله / أو اسم النشاط التجاري") },
                        placeholder = { Text("مثال: سامسونج S22 Ultra أو متجر إلكتروني") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp)
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("تفاصيل المشكلة أو المتطلبات") },
                        placeholder = { Text("اكتب تفاصيل مشكلة الهاتف، كود الخطأ، أو متطلبات حملتك الإعلانية...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = textFieldColors,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextWhite
                            )
                        ) {
                            Text("إلغاء")
                        }

                        Button(
                            onClick = {
                                val displayName = if (clientName.isNotBlank()) "$clientName - " else ""
                                onSubmit(
                                    "$selectedSubCategory - $displayName$deviceOrBusiness",
                                    selectedCategory,
                                    selectedSubCategory,
                                    deviceOrBusiness.ifBlank { "غير محدد" },
                                    clientPhone.ifBlank { "غير محدد" },
                                    (if (clientName.isNotBlank()) "اسم العميل: $clientName\n" else "") + notes.ifBlank { "لا توجد ملاحظات إضافية" }
                                )
                                isSubmitted = true
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                contentColor = ImmersiveBg
                            )
                        ) {
                            Text("تأكيد الطلب", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

