package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.OpenInBrowser
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountRecoveryWizardDialog(
    onDismiss: () -> Unit,
    onRequestDirectHelp: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var selectedService by remember { mutableStateOf("whatsapp") } // whatsapp, google, facebook
    var phoneNumber by remember { mutableStateOf("+967 ") }
    var emailAddress by remember { mutableStateOf("") }
    var banReason by remember { mutableStateOf("حظر مؤقت أو غير مقصود (رسائل جماعية)") }
    var banReasonExpanded by remember { mutableStateOf(false) }

    val banReasons = listOf(
        "حظر مؤقت أو غير مقصود (رسائل جماعية)",
        "حظر دائم بسبب استخدام نسخ غير رسمية",
        "تم الإبلاغ عن الرقم عن طريق الخطأ",
        "اختراق الحساب وسرقة جلسة الدخول"
    )

    val generatedAppealMessage = remember(phoneNumber, banReason) {
        """
        السادة فريق دعم واتساب المحترمين،
        تحية طيبة وبعد،،
        أرجو من سيادتكم التكرم بإعادة تفعيل ومراجعة حسابي على تطبيق واتساب المرتبط بالرقم: ($phoneNumber).
        تم إيقاف الحساب دون قصد مني أو انتهاك متعمد لشروط الخدمة، وأؤكد التزامي التام بكافة السياسات والقوانين الخاصة بمنصة واتساب.
        الرقم مهم جداً لتواصلي اليومي والعملي، شاكراً لكم حسن تعاونكم وسرعة استجابتكم.
        
        Dear WhatsApp Support Team,
        My WhatsApp account with phone number ($phoneNumber) was banned by mistake. I have not violated WhatsApp Terms of Service intentionally. This number is vital for my personal and business communications. Please review and unban my account as soon as possible.
        Thank you.
        """.trimIndent()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ImmersiveSurfaceCard,
            border = BorderStroke(1.dp, ImmersiveBorder),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentPurpleContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ManageAccounts, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(
                                text = "معالج استرجاع الحسابات وفك الحظر",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "صانع رسائل فك حظر واتساب واسترداد الحسابات",
                                fontSize = 11.sp,
                                color = AccentPurple
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSlate)
                    }
                }

                // Account Type Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedService == "whatsapp",
                        onClick = { selectedService = "whatsapp" },
                        label = { Text("فك حظر واتساب", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = com.example.ui.theme.ImmersiveBg
                        )
                    )
                    FilterChip(
                        selected = selectedService == "google",
                        onClick = { selectedService = "google" },
                        label = { Text("استرجاع Google", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = com.example.ui.theme.ImmersiveBg
                        )
                    )
                }

                if (selectedService == "whatsapp") {
                    // WhatsApp Unban Wizard
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("رقم الواتساب المحظور مع رمز الدولة") },
                        placeholder = { Text("+967 77xxxxxxx") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedLabelColor = EmeraldPrimary,
                            unfocusedLabelColor = TextSlate,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = EmeraldPrimary
                        )
                    )

                    ExposedDropdownMenuBox(
                        expanded = banReasonExpanded,
                        onExpandedChange = { banReasonExpanded = !banReasonExpanded }
                    ) {
                        OutlinedTextField(
                            value = banReason,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("سبب الحظر المرجح") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = banReasonExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = ImmersiveBorder,
                                focusedLabelColor = EmeraldPrimary,
                                unfocusedLabelColor = TextSlate,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = banReasonExpanded,
                            onDismissRequest = { banReasonExpanded = false },
                            modifier = Modifier.background(ImmersiveSurfaceCard)
                        ) {
                            banReasons.forEach { reason ->
                                DropdownMenuItem(
                                    text = { Text(reason, fontSize = 12.sp, color = TextWhite) },
                                    onClick = {
                                        banReason = reason
                                        banReasonExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Formatted Message Preview
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "صيغة الطعن الرسمية الجاهزة:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(generatedAppealMessage))
                                    Toast.makeText(context, "تم نسخ صيغة الطعن!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                }
                            }

                            Text(
                                text = generatedAppealMessage,
                                fontSize = 11.sp,
                                color = TextSlate,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Direct Send Buttons
                    Button(
                        onClick = {
                            AppIntentUtils.sendEmail(
                                context = context,
                                toEmail = "support@support.whatsapp.com",
                                subject = "Account Banned by mistake: $phoneNumber",
                                body = generatedAppealMessage
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = com.example.ui.theme.ImmersiveBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إرسال رسالة الطعن لدعم واتساب عبر البريد", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Google Recovery Guide
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "خطوات استرجاع حساب Google / Gmail:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                            val steps = listOf(
                                "1. التوجه لصفحة استرداد حسابات Google الرسمية (Recovery).",
                                "2. إدخال عنوان البريد الإلكتروني أو رقم الهاتف المرتبط بالحساب.",
                                "3. الضغط على 'تجربة طريقة أخرى' في حال عدم تذكر كلمة المرور.",
                                "4. إدخال رمز التحقق المرسل لرقم الهاتف أو البريد الاحتياطي.",
                                "5. كتابة كلمة مرور جديدة قوية وتفعيل التحقق بخطوتين فوراً."
                            )
                            steps.forEach { step ->
                                Text(text = step, fontSize = 11.sp, color = TextSlate, lineHeight = 16.sp)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            AppIntentUtils.openWebUrl(context, "https://accounts.google.com/signin/recovery")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = com.example.ui.theme.ImmersiveBg
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("فتح بوابة استرداد حسابات Google الرسمية", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = onRequestDirectHelp,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("طلب تدخل واسترجاع مباشر من خبراء رقيب", fontSize = 12.sp)
                }
            }
        }
    }
}
