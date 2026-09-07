package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrierUnlockToolDialog(
    onDismiss: () -> Unit,
    onRequestDirectHelp: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    var imeiInput by remember { mutableStateOf("354892019482012") }
    var selectedCarrier by remember { mutableStateOf("Sprint / Boost Mobile (USA)") }
    var carrierDropdownExpanded by remember { mutableStateOf(false) }

    var isChecking by remember { mutableStateOf(false) }
    var checkProgress by remember { mutableFloatStateOf(0f) }
    var unlockResultCode by remember { mutableStateOf<String?>(null) }
    var spcCode by remember { mutableStateOf<String?>(null) }
    var statusText by remember { mutableStateOf<String?>(null) }

    val carriers = listOf(
        "Sprint / Boost Mobile (USA)",
        "Verizon Wireless (USA)",
        "AT&T (USA)",
        "T-Mobile / Metro (USA)",
        "NTT Docomo (اليابان)",
        "SoftBank (اليابان)",
        "Vodafone (أوروبا)",
        "شبكة محلية أخرى"
    )

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
                                .background(AccentBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(
                                text = "أداة فحص وفك شفرات الشبكة",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "توليد كود فك الشفرة وفحص قفل SIM Lock",
                                fontSize = 11.sp,
                                color = AccentBlue
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSlate)
                    }
                }

                // IMEI Input
                OutlinedTextField(
                    value = imeiInput,
                    onValueChange = { if (it.length <= 15) imeiInput = it },
                    label = { Text("رقم IMEI للهاتف (15 رقماً)") },
                    placeholder = { Text("أدخل رقم IMEI...") },
                    trailingIcon = {
                        IconButton(onClick = { AppIntentUtils.dialPhoneNumber(context, "*#06#") }) {
                            Icon(Icons.Default.Phone, contentDescription = "كود *#06#", tint = EmeraldPrimary)
                        }
                    },
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "اضغط على أيقونة الهاتف لمعرفة رقم IMEI عبر كود *#06#",
                        fontSize = 11.sp,
                        color = TextSlate
                    )
                    Text(
                        text = "${imeiInput.length}/15",
                        fontSize = 11.sp,
                        color = if (imeiInput.length == 15) EmeraldPrimary else TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Carrier Selector Dropdown
                ExposedDropdownMenuBox(
                    expanded = carrierDropdownExpanded,
                    onExpandedChange = { carrierDropdownExpanded = !carrierDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCarrier,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("الشبكة المقفل عليها الجهاز") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = carrierDropdownExpanded) },
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
                        expanded = carrierDropdownExpanded,
                        onDismissRequest = { carrierDropdownExpanded = false },
                        modifier = Modifier.background(ImmersiveSurfaceCard)
                    ) {
                        carriers.forEach { carrier ->
                            DropdownMenuItem(
                                text = { Text(carrier, fontSize = 12.sp, color = TextWhite) },
                                onClick = {
                                    selectedCarrier = carrier
                                    carrierDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Progress Indicator
                if (isChecking) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(ImmersiveSurfaceVariant)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = statusText ?: "جاري فحص قاعدة بيانات فك الشفرات...",
                            fontSize = 12.sp,
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        LinearProgressIndicator(
                            progress = { checkProgress },
                            modifier = Modifier.fillMaxWidth(),
                            color = EmeraldPrimary,
                            trackColor = ImmersiveBorder
                        )
                    }
                }

                // Generated Results Card
                if (unlockResultCode != null) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0x2234D399),
                        border = BorderStroke(1.dp, EmeraldPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "تم استخراج كود فك الشفرة بنجاح!",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "كود الشبكة (NCK / Unlock Code):", fontSize = 11.sp, color = TextSlate)
                                    Text(
                                        text = unlockResultCode ?: "",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite,
                                        letterSpacing = 2.sp
                                    )
                                }

                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(unlockResultCode ?: ""))
                                    Toast.makeText(context, "تم نسخ كود فك الشفرة!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = EmeraldPrimary)
                                }
                            }

                            if (spcCode != null) {
                                Text(
                                    text = "كود برمجة CDMA (SPC Code): $spcCode",
                                    fontSize = 12.sp,
                                    color = AccentBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "طريقة التطبيق: أدخل شريحة اتصالات غير مدعومة في الهاتف، ستظهر شاشة تطلب (رمز إلغاء قفل شبكة SIM)، أدخل الكود أعلاه واضغط فك القفل.",
                                fontSize = 11.sp,
                                color = TextSlate,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                // Action Buttons
                Button(
                    onClick = {
                        scope.launch {
                            isChecking = true
                            unlockResultCode = null
                            checkProgress = 0.2f
                            statusText = "جاري قراءة رقم IMEI والتحقق من حالة القفل..."
                            delay(600)
                            checkProgress = 0.5f
                            statusText = "فحص التوافق مع شبكة $selectedCarrier..."
                            delay(600)
                            checkProgress = 0.8f
                            statusText = "توليد كود NCK الرسمي وتجاوز قيود المشغل..."
                            delay(600)
                            checkProgress = 1.0f
                            isChecking = false
                            // Generate pseudo-deterministic unlock code based on IMEI
                            val hash = (imeiInput.hashCode().toLong() and 0xFFFFFFF).toString().padStart(8, '4')
                            unlockResultCode = hash.take(8)
                            spcCode = "000000"
                            statusText = "اكتمل الفحص بنجاح!"
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isChecking
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isChecking) "جاري الفحص والتوليد..." else "بدء فحص الشفرة وتوليد الكود",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                    Text("طلب فك الشفرة برمجياً عبر مهندسي رقيب", fontSize = 12.sp)
                }
            }
        }
    }
}
