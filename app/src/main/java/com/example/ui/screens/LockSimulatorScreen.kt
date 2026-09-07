package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPinkContainer
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TealGradientDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import com.example.util.LockSimulatorPreferences
import com.example.util.SecurityConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockSimulatorScreen(
    onBackToPhoneServices: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val prefs = remember { LockSimulatorPreferences(context) }

    var securityConfig by remember { mutableStateOf(prefs.getSecurityConfig()) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: المحاكي, 1: تخصيص السؤال والرمز, 2: أدوات FRP

    // Simulator State
    var isPhoneUnlocked by remember { mutableStateOf(false) }
    var isVerifyingOwnership by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinErrorMessage by remember { mutableStateOf<String?>(null) }

    // Ownership Verification State
    var userAnswerInput by remember { mutableStateOf("") }
    var verificationResult by remember { mutableStateOf<Boolean?>(null) } // true = success, false = wrong, null = pending
    var showHint by remember { mutableStateOf(false) }
    var verificationAttempts by remember { mutableIntStateOf(0) }

    // Settings tab state
    var editPin by remember { mutableStateOf(securityConfig.pin) }
    var editOwnerName by remember { mutableStateOf(securityConfig.ownerName) }
    var editQuestion by remember { mutableStateOf(securityConfig.question) }
    var editAnswer by remember { mutableStateOf(securityConfig.answer) }
    var editHint by remember { mutableStateOf(securityConfig.hint) }
    var questionDropdownExpanded by remember { mutableStateOf(false) }

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 36.dp)
    ) {
        // Header & Back
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onBackToPhoneServices,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkButtonBg)
                            .border(1.dp, DarkButtonBorder, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                    }

                    Column {
                        Text(
                            text = "محاكي فك وتذكير كلمة المرور",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "إثبات ملكية الهاتف واستعادة رمز القفل المنسي",
                            fontSize = 12.sp,
                            color = EmeraldPrimary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0x3334D399),
                    border = BorderStroke(1.dp, Color(0x6634D399))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(13.dp))
                        Text("ميزة ذكية فعالة", fontSize = 11.sp, color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Hero Info Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, Color(0x3334D399)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(EmeraldDark, TealGradientDark)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(20.dp))
                            Text(
                                text = "كيف تعمل خدمة التذكير بعد إثبات الملكية؟",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "عند نسيان رمز PIN أو كلمة مرور الهاتف، يطلب منك النظام الإجابة على سؤال الأمان السري المحدد مسبقاً. بمجرد مطابقة إجابتك، يتم تأكيد إثبات ملكيتك للهاتف ويقوم التطبيق بتذكيرك بكلمة المرور فوراً وإلغاء القفل بأمان تام.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = EmeraldLight.copy(alpha = 0.95f)
                        )
                    }
                }
            }
        }

        // Secondary Tabs
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf(
                        "📱 تجربة المحاكي",
                        "⚙️ تخصيص السؤال والرمز",
                        "🛠️ أدوات FRP المتطورة"
                    )

                    tabs.forEachIndexed { index, label ->
                        val isSelected = selectedTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) EmeraldPrimary else Color.Transparent)
                                .clickable { selectedTab = index }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) ImmersiveBg else TextSlate,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // TAB 0: Interactive Lockscreen Simulator
                item {
                    SimulatorTab(
                        securityConfig = securityConfig,
                        isPhoneUnlocked = isPhoneUnlocked,
                        isVerifyingOwnership = isVerifyingOwnership,
                        enteredPin = enteredPin,
                        pinErrorMessage = pinErrorMessage,
                        userAnswerInput = userAnswerInput,
                        verificationResult = verificationResult,
                        showHint = showHint,
                        verificationAttempts = verificationAttempts,
                        onPinDigitClick = { digit ->
                            if (enteredPin.length < 6) {
                                enteredPin += digit
                                pinErrorMessage = null
                                // Auto check if reaches 4 digits
                                if (enteredPin == securityConfig.pin) {
                                    isPhoneUnlocked = true
                                    enteredPin = ""
                                    pinErrorMessage = null
                                } else if (enteredPin.length == securityConfig.pin.length) {
                                    pinErrorMessage = "رمز PIN غير صحيح! حاول مجدداً أو استخدم إثبات الملكية."
                                }
                            }
                        },
                        onPinBackspace = {
                            if (enteredPin.isNotEmpty()) {
                                enteredPin = enteredPin.dropLast(1)
                                pinErrorMessage = null
                            }
                        },
                        onForgotLockClick = {
                            isVerifyingOwnership = true
                            userAnswerInput = ""
                            verificationResult = null
                            showHint = false
                        },
                        onUserAnswerChange = { userAnswerInput = it },
                        onVerifyAnswerClick = {
                            val isValid = prefs.isAnswerValid(userAnswerInput, securityConfig.answer)
                            verificationAttempts++
                            verificationResult = isValid
                            if (isValid) {
                                Toast.makeText(context, "تم إثبات ملكية الهاتف بنجاح!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onToggleHint = { showHint = !showHint },
                        onUnlockNow = {
                            isPhoneUnlocked = true
                            isVerifyingOwnership = false
                            verificationResult = null
                            userAnswerInput = ""
                        },
                        onRelock = {
                            isPhoneUnlocked = false
                            isVerifyingOwnership = false
                            verificationResult = null
                            enteredPin = ""
                            userAnswerInput = ""
                            pinErrorMessage = null
                        },
                        onSwitchToSettingsTab = { selectedTab = 1 },
                        onCopyPin = {
                            clipboardManager.setText(AnnotatedString(securityConfig.pin))
                            Toast.makeText(context, "تم نسخ رمز PIN: ${securityConfig.pin}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            1 -> {
                // TAB 1: Customizing Security Question and PIN
                item {
                    SettingsTab(
                        editPin = editPin,
                        editOwnerName = editOwnerName,
                        editQuestion = editQuestion,
                        editAnswer = editAnswer,
                        editHint = editHint,
                        questionDropdownExpanded = questionDropdownExpanded,
                        textFieldColors = textFieldColors,
                        onPinChange = { editPin = it },
                        onOwnerNameChange = { editOwnerName = it },
                        onQuestionChange = { editQuestion = it },
                        onAnswerChange = { editAnswer = it },
                        onHintChange = { editHint = it },
                        onDropdownToggle = { questionDropdownExpanded = !questionDropdownExpanded },
                        onSelectPredefinedQuestion = {
                            editQuestion = it
                            questionDropdownExpanded = false
                        },
                        onSaveConfig = {
                            if (editPin.isBlank() || editAnswer.isBlank()) {
                                Toast.makeText(context, "يرجى كتابة رمز PIN وإجابة سؤال الأمان!", Toast.LENGTH_SHORT).show()
                            } else {
                                val newConfig = SecurityConfig(
                                    pin = editPin.trim(),
                                    question = editQuestion.trim(),
                                    answer = editAnswer.trim(),
                                    ownerName = editOwnerName.trim().ifBlank { "مالك الهاتف" },
                                    hint = editHint.trim().ifBlank { "رمز PIN مكون من 4 أرقام" }
                                )
                                prefs.saveSecurityConfig(newConfig)
                                securityConfig = newConfig
                                Toast.makeText(context, "تم حفظ الإعدادات بنجاح! يمكنك تجربتها الآن في المحاكي.", Toast.LENGTH_LONG).show()
                                selectedTab = 0 // Return to simulator to test!
                            }
                        },
                        onResetDefault = {
                            val defaultConfig = SecurityConfig(
                                pin = "2024",
                                question = LockSimulatorPreferences.DEFAULT_QUESTIONS[0],
                                answer = "اليرموك",
                                ownerName = "المهندس عبدالرقيب",
                                hint = "رمز مكون من 4 أرقام (سنة مهمة)"
                            )
                            prefs.saveSecurityConfig(defaultConfig)
                            securityConfig = defaultConfig
                            editPin = defaultConfig.pin
                            editQuestion = defaultConfig.question
                            editAnswer = defaultConfig.answer
                            editOwnerName = defaultConfig.ownerName
                            editHint = defaultConfig.hint
                            Toast.makeText(context, "تمت استعادة الإعدادات الافتراضية للتجربة.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            2 -> {
                // TAB 2: FRP & Lock Bypass Tech Guide & Commands
                item {
                    FrpTechToolsTab(
                        onContactWhatsApp = {
                            AppIntentUtils.openWhatsApp(
                                context,
                                "مرحباً مركز رقيب، لدي هاتف مقفل برمز شاشة / حساب جوجل FRP وأود المساعدة في فك القفل."
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SimulatorTab(
    securityConfig: SecurityConfig,
    isPhoneUnlocked: Boolean,
    isVerifyingOwnership: Boolean,
    enteredPin: String,
    pinErrorMessage: String?,
    userAnswerInput: String,
    verificationResult: Boolean?,
    showHint: Boolean,
    verificationAttempts: Int,
    onPinDigitClick: (String) -> Unit,
    onPinBackspace: () -> Unit,
    onForgotLockClick: () -> Unit,
    onUserAnswerChange: (String) -> Unit,
    onVerifyAnswerClick: () -> Unit,
    onToggleHint: () -> Unit,
    onUnlockNow: () -> Unit,
    onRelock: () -> Unit,
    onSwitchToSettingsTab: () -> Unit,
    onCopyPin: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(2.dp, if (isPhoneUnlocked) EmeraldPrimary else ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Phone Frame Header Simulation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "12:45",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSlate
                )

                // Speaker notch
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFF262D3D))
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "4G LTE", fontSize = 10.sp, color = TextSlate, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = if (isPhoneUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isPhoneUnlocked) EmeraldPrimary else AccentOrange,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            if (isPhoneUnlocked) {
                // PHONE UNLOCKED STATE (Simulated Home Screen)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0x3334D399))
                            .border(2.dp, EmeraldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "تم فك قفل الهاتف بنجاح!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "مرحباً بك: ${securityConfig.ownerName}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldLight
                            )
                            Text(
                                text = "أنت الآن داخل الشاشة الرئيسية للهاتف بعد التأكيد من ملكيتك وتذكر الرمز (${securityConfig.pin}).",
                                fontSize = 12.sp,
                                color = TextSlate,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Simulated Apps Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MockAppIcon(Icons.Default.Phone, "الهاتف", EmeraldPrimary)
                        MockAppIcon(Icons.Default.Chat, "واتساب", AccentTeal)
                        MockAppIcon(Icons.Default.Photo, "المعرض", AccentPink)
                        MockAppIcon(Icons.Default.CameraAlt, "الكاميرا", AccentBlue)
                        MockAppIcon(Icons.Default.Settings, "الضبط", AccentOrange)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = onRelock,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkButtonBg,
                            contentColor = TextWhite
                        ),
                        border = BorderStroke(1.dp, DarkButtonBorder)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إعادة قفل الهاتف للتجربة مجدداً", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (isVerifyingOwnership) {
                // VERIFYING OWNERSHIP FLOW (The User's core feature!)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(AccentOrangeContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "التحقق من ملكية الهاتف",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = "لحماية خصوصية صاحب الهاتف، يرجى الإجابة على سؤال الأمان المسجل مسبقاً لتذكيرك بكلمة المرور:",
                        fontSize = 12.sp,
                        color = TextSlate,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    // Security Question Card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, Color(0x33F59E0B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "سؤال الأمان المطلوب:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentOrange
                                )
                            }
                            Text(
                                text = securityConfig.question,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                        }
                    }

                    // Answer Input Field
                    OutlinedTextField(
                        value = userAnswerInput,
                        onValueChange = onUserAnswerChange,
                        label = { Text("اكتب إجابتك هنا لإثبات الملكية") },
                        placeholder = { Text("مثال: ${securityConfig.answer}") },
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

                    // Hint toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showHint) "تلميح: ${securityConfig.hint}" else "هل تحتاج إلى تلميح؟",
                            fontSize = 11.sp,
                            color = if (showHint) EmeraldLight else TextMuted
                        )
                        OutlinedButton(
                            onClick = onToggleHint,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder)
                        ) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(13.dp), tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (showHint) "إخفاء" else "تلميح", fontSize = 10.sp, color = TextWhite)
                        }
                    }

                    // Verification Result Cards
                    if (verificationResult == true) {
                        // SUCCESS: REMIND PASSWORD & UNLOCK
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0x2634D399),
                            border = BorderStroke(1.5.dp, EmeraldPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(24.dp))
                                    Text(
                                        text = "تم إثبات ملكية الهاتف بنجاح!",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                }

                                Text(
                                    text = "مرحباً ${securityConfig.ownerName}، تم تأكيد هويتك. كلمة المرور / رمز PIN الخاص بهاتفك هو:",
                                    fontSize = 12.sp,
                                    color = TextWhite,
                                    textAlign = TextAlign.Center
                                )

                                // Password Reminder Display Box
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = ImmersiveBg,
                                    border = BorderStroke(1.dp, EmeraldPrimary),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = "رمز PIN السري:", fontSize = 11.sp, color = TextSlate)
                                            Text(
                                                text = securityConfig.pin,
                                                fontSize = 26.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                letterSpacing = 4.sp,
                                                color = EmeraldPrimary
                                            )
                                        }

                                        IconButton(onClick = onCopyPin) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = EmeraldPrimary)
                                        }
                                    }
                                }

                                // Unlock Button
                                Button(
                                    onClick = onUnlockNow,
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldPrimary,
                                        contentColor = ImmersiveBg
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.LockOpen, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("إلغاء قفل الهاتف والدخول للشاشة الآن", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else if (verificationResult == false) {
                        // FAILED ANSWER
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0x26EF4444),
                            border = BorderStroke(1.dp, Color(0x66EF4444)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "❌ الإجابة غير صحيحة!",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF87171)
                                )
                                Text(
                                    text = "لم تتطابق الإجابة مع البيانات المسجلة لهذا الهاتف. يرجى مراجعة إجابتك أو تعديلها من تبويب الإعدادات.",
                                    fontSize = 11.sp,
                                    color = TextSlate,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onRelock,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextSlate
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("رجوع لشاشة القفل", fontSize = 12.sp)
                        }

                        Button(
                            onClick = onVerifyAnswerClick,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPrimary,
                                contentColor = ImmersiveBg
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تأكيد الإجابة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // ACTIVE LOCKED SCREEN (Standard Lockscreen Simulator)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x22F59E0B))
                            .border(1.dp, Color(0x66F59E0B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "الهاتف مقفل برمز PIN",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "أدخل الرمز أو اضغط على 'نسيت كلمة المرور' لتجربة استعادة الملكية",
                            fontSize = 11.sp,
                            color = TextSlate,
                            textAlign = TextAlign.Center
                        )
                    }

                    // PIN Dots Display
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        for (i in 0 until 4) {
                            val isFilled = i < enteredPin.length
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (isFilled) EmeraldPrimary else Color.Transparent)
                                    .border(1.5.dp, if (isFilled) EmeraldPrimary else ImmersiveBorder, CircleShape)
                            )
                        }
                    }

                    // Error message if any
                    if (pinErrorMessage != null) {
                        Text(
                            text = pinErrorMessage,
                            fontSize = 11.sp,
                            color = Color(0xFFF87171),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Numeric Keypad Simulation
                    Column(
                        modifier = Modifier.width(260.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val keypad = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("del", "0", "ok")
                        )

                        keypad.forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                row.forEach { key ->
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(DarkButtonBg)
                                            .border(1.dp, DarkButtonBorder, CircleShape)
                                            .clickable {
                                                when (key) {
                                                    "del" -> onPinBackspace()
                                                    "ok" -> {
                                                        if (enteredPin == securityConfig.pin) {
                                                            onUnlockNow()
                                                        } else {
                                                            // failed
                                                        }
                                                    }
                                                    else -> onPinDigitClick(key)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when (key) {
                                            "del" -> Icon(Icons.Default.Backspace, contentDescription = "حذف", tint = TextSlate, modifier = Modifier.size(20.dp))
                                            "ok" -> Icon(Icons.Default.Check, contentDescription = "تأكيد", tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                                            else -> Text(text = key, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // THE FORGOT PASSWORD ACTION (Core trigger)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0x1AF59E0B),
                        border = BorderStroke(1.dp, Color(0x4DF59E0B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onForgotLockClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(18.dp))
                                Column {
                                    Text(
                                        text = "هل نسيت كلمة المرور أو رمز القفل؟",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Text(
                                        text = "اضغط هنا لإثبات ملكية الهاتف عبر سؤال الأمان وتذكر الرمز",
                                        fontSize = 10.sp,
                                        color = AccentOrange
                                    )
                                }
                            }
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(18.dp))
                        }
                    }

                    // Quick Settings Shortcut
                    OutlinedButton(
                        onClick = onSwitchToSettingsTab,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, DarkButtonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextSlate
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = AccentTeal)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تعديل الرمز وسؤال الأمان المحفوظ", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTab(
    editPin: String,
    editOwnerName: String,
    editQuestion: String,
    editAnswer: String,
    editHint: String,
    questionDropdownExpanded: Boolean,
    textFieldColors: androidx.compose.material3.TextFieldColors,
    onPinChange: (String) -> Unit,
    onOwnerNameChange: (String) -> Unit,
    onQuestionChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onHintChange: (String) -> Unit,
    onDropdownToggle: () -> Unit,
    onSelectPredefinedQuestion: (String) -> Unit,
    onSaveConfig: () -> Unit,
    onResetDefault: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AccentTeal.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = AccentTeal, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text(
                        text = "تخصيص سؤال الأمان ورمز الهاتف",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "يتم حفظ هذه البيانات محلياً على جهازك لتختبرها في المحاكي بأي وقت",
                        fontSize = 11.sp,
                        color = TextSlate
                    )
                }
            }

            // PIN Field
            OutlinedTextField(
                value = editPin,
                onValueChange = onPinChange,
                label = { Text("رمز PIN أو كلمة مرور الهاتف للتذكير بها") },
                placeholder = { Text("مثال: 2024") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(14.dp)
            )

            // Owner Name Field
            OutlinedTextField(
                value = editOwnerName,
                onValueChange = onOwnerNameChange,
                label = { Text("اسم صاحب الهاتف") },
                placeholder = { Text("مثال: عبدالرقيب فرج") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(14.dp)
            )

            // Predefined Questions Dropdown
            ExposedDropdownMenuBox(
                expanded = questionDropdownExpanded,
                onExpandedChange = { onDropdownToggle() }
            ) {
                OutlinedTextField(
                    value = editQuestion,
                    onValueChange = onQuestionChange,
                    readOnly = false,
                    label = { Text("سؤال الأمان (اختر أو اكتب سؤالك الخاص)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = questionDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = textFieldColors,
                    shape = RoundedCornerShape(14.dp)
                )

                ExposedDropdownMenu(
                    expanded = questionDropdownExpanded,
                    onDismissRequest = onDropdownToggle,
                    modifier = Modifier.background(ImmersiveSurfaceCard)
                ) {
                    LockSimulatorPreferences.DEFAULT_QUESTIONS.forEach { q ->
                        DropdownMenuItem(
                            text = { Text(q, fontSize = 12.sp, color = TextWhite) },
                            onClick = { onSelectPredefinedQuestion(q) }
                        )
                    }
                }
            }

            // Answer Field
            OutlinedTextField(
                value = editAnswer,
                onValueChange = onAnswerChange,
                label = { Text("الإجابة الصحيحة لسؤال الأمان (السرية)") },
                placeholder = { Text("اكتب إجابتك التي تثبت بها ملكيتك...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(14.dp)
            )

            // Hint Field
            OutlinedTextField(
                value = editHint,
                onValueChange = onHintChange,
                label = { Text("تلميح اختياري يساعدك على التذكر") },
                placeholder = { Text("مثال: سنة التخرج أو اسم المدرسة الابتدائية") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors,
                shape = RoundedCornerShape(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onResetDefault,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextSlate
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("الافتراضي", fontSize = 12.sp)
                }

                Button(
                    onClick = onSaveConfig,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier.weight(2f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حفظ البيانات وتجربتها فوراً", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FrpTechToolsTab(
    onContactWhatsApp: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AccentOrangeContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(24.dp))
                }
                Column {
                    Text(
                        text = "أدوات وطرق تخطي قفل FRP وحساب جوجل",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "دليل فني شامل لمهندسي الصيانة والمستخدمين",
                        fontSize = 11.sp,
                        color = TextSlate
                    )
                }
            }

            val frpMethods = listOf(
                "أجهزة سامسونج (Samsung):" to "الدخول لوضع Download Mode وتفليش ملف Combination أو استخدام أداة SamFw لتفعيل ADB عبر كود الطوارئ *#0*# وضغط زر Remove FRP بضغطة زر.",
                "أجهزة شياومي وبوكو (Xiaomi / Poco):" to "تخطي قفل Mi Account وFRP عبر وضع Fastboot أو تفليش ملف Persist عبر أداة Mi Flash Tool، أو استرداد الحساب برقم الشريحة.",
                "أجهزة كوالكوم وميدياتك (Qualcomm / MTK):" to "التوصيل بنظام EDL 9008 أو وضع BROM عبر كبسة زري الصوت معاً لاستخراج وحذف حماية FRP بنظام UnlockTool.",
                "طريقة TalkBack والثغرات اليدوية:" to "تفعيل وضع المكفوفين بالضغط على زري الصوت، رسم حرف L والدخول لإعدادات المساعدة وتشغيل متصفح Chrome لتثبيت تطبيق Apex Launcher وBypass APK."
            )

            frpMethods.forEach { (brand, guide) ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ImmersiveSurfaceVariant,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = brand,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal
                        )
                        Text(
                            text = guide,
                            fontSize = 12.sp,
                            color = TextSlate,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Warning note
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x1AF59E0B))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(18.dp))
                Text(
                    text = "تنويه: جميع خدمات فك الإقفال وFRP تتطلب إثبات ملكية الجهاز بشكل قانوني لحماية أمن وخصوصية مالك الهاتف.",
                    fontSize = 11.sp,
                    color = TextSlate,
                    lineHeight = 16.sp
                )
            }

            Button(
                onClick = onContactWhatsApp,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    contentColor = ImmersiveBg
                )
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("طلب فك قفل هاتف مستعصٍ من خبراء رقيب بالواتساب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MockAppIcon(icon: ImageVector, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(color.copy(alpha = 0.2f))
                .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        }
        Text(text = label, fontSize = 10.sp, color = TextWhite)
    }
}
