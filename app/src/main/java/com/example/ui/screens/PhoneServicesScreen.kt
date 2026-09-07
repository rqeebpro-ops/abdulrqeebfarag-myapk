package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PhoneProblemType
import com.example.model.PhoneServiceDetail
import com.example.model.ServiceSection
import com.example.ui.dialogs.AccountRecoveryWizardDialog
import com.example.ui.dialogs.ArabizationToolDialog
import com.example.ui.dialogs.CarrierUnlockToolDialog
import com.example.ui.dialogs.PhoneDetailDialog
import com.example.ui.dialogs.RequestServiceDialog
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPinkContainer
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.AccentTealContainer
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
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

@Composable
fun PhoneServicesScreen(
    services: List<PhoneServiceDetail>,
    selectedFilter: PhoneProblemType?,
    onFilterChange: (PhoneProblemType?) -> Unit,
    onNavigateSection: (ServiceSection) -> Unit,
    onSubmitRequest: (title: String, category: String, subCategory: String, device: String, phone: String, notes: String) -> Unit
) {
    val context = LocalContext.current
    var selectedDetailService by remember { mutableStateOf<PhoneServiceDetail?>(null) }
    var serviceToRequest by remember { mutableStateOf<PhoneServiceDetail?>(null) }
    var showCarrierUnlockTool by remember { mutableStateOf(false) }
    var showAccountRecoveryTool by remember { mutableStateOf(false) }
    var showArabizationTool by remember { mutableStateOf(false) }

    val filteredServices = remember(services, selectedFilter) {
        if (selectedFilter == null) services else services.filter { it.type == selectedFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Hero Card with Immersive Emerald-Teal Gradient and System Status
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
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
                        .padding(20.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0x33FFFFFF))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FiberManualRecord,
                                    contentDescription = null,
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "حالة النظام: نشط ومفعّل",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "مركز رقيب",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldLight
                            )
                        }

                        Text(
                            text = "صيانة وبرمجة الهواتف الذكية",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "كل الأدوات والحلول التقنية جاهزة لحل مشاكل هاتفك: محاكي تذكير وفك القفل عبر إثبات الملكية، فك الشفرات، استرجاع الحسابات، فحص واسترداد البيانات، التعريب، وتفعيل 4G.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = EmeraldLight.copy(alpha = 0.95f)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Button(
                                onClick = { onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR) },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentOrange,
                                    contentColor = ImmersiveBg
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("محاكي فك القفل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER) },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0x33FFFFFF),
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color(0x66FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("فحص البيانات", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { onNavigateSection(ServiceSection.APN_SETTINGS) },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0x22000000),
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color(0x44FFFFFF)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.CellTower, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("تفعيل 4G", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Quick Feature Highlights (Immersive UI 3 grid cards)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Lock Recovery Simulator (The requested feature!)
                Card(
                    onClick = { onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, Color(0x66F59E0B)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentOrangeContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Key,
                                contentDescription = null,
                                tint = AccentOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "محاكي القفل",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "تذكير الرمز بإثبات الملكية",
                            fontSize = 10.sp,
                            color = AccentOrange,
                            lineHeight = 14.sp
                        )
                    }
                }

                // Card 2: Data Recovery
                Card(
                    onClick = { onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentPinkContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.RestorePage,
                                contentDescription = null,
                                tint = AccentPink,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "استرداد البيانات",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "فحص الصور والملفات",
                            fontSize = 10.sp,
                            color = TextSlate,
                            lineHeight = 14.sp
                        )
                    }
                }

                // Card 3: 4G APN
                Card(
                    onClick = { onNavigateSection(ServiceSection.APN_SETTINGS) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentTealContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.CellTower,
                                contentDescription = null,
                                tint = AccentTeal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "تفعيل 4G",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "نقاط وصول الشبكات",
                            fontSize = 10.sp,
                            color = TextSlate,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Dedicated Banner for Login & Subscription
        item {
            Card(
                onClick = { onNavigateSection(ServiceSection.LOGIN_SUBSCRIPTION) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(EmeraldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "تسجيل الدخول والاشتراك بالخدمات",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldPrimary.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "2000 ر.ي",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "سجل بياناتك ونوع هاتفك وادفع عبر الكريمي، جيب، ون كاش، أو جوالي لتفعيل فوري",
                                fontSize = 11.sp,
                                color = TextSlate,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateSection(ServiceSection.LOGIN_SUBSCRIPTION) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = ImmersiveBg
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("اشتراك", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Filter chips row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "أقسام ومشاكل الهواتف:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { onFilterChange(null) },
                            label = { Text("جميع الخدمات", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (selectedFilter == null) EmeraldPrimary else ImmersiveBorder),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = ImmersiveSurfaceCard,
                                labelColor = TextSlate,
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = ImmersiveBg
                            )
                        )
                    }
                    items(PhoneProblemType.values()) { type ->
                        val isSelected = selectedFilter == type
                        FilterChip(
                            selected = isSelected,
                            onClick = { onFilterChange(if (isSelected) null else type) },
                            label = { Text(type.titleAr, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getIconForProblemType(type),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) ImmersiveBg else getAccentColorForType(type)
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else ImmersiveBorder),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = ImmersiveSurfaceCard,
                                labelColor = TextSlate,
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = ImmersiveBg
                            )
                        )
                    }
                }
            }
        }

        // Service Items
        items(filteredServices) { service ->
            PhoneServiceCard(
                service = service,
                onViewDetails = { selectedDetailService = service },
                onRequestService = { serviceToRequest = service },
                onLaunchInteractiveTool = {
                    when (service.type) {
                        PhoneProblemType.LOCK_BYPASS -> onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR)
                        PhoneProblemType.FAILURE_LOCK_SIMULATOR -> onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR)
                        PhoneProblemType.CARRIER_UNLOCK -> showCarrierUnlockTool = true
                        PhoneProblemType.ACCOUNT_RECOVERY -> showAccountRecoveryTool = true
                        PhoneProblemType.DATA_RECOVERY -> onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER)
                        PhoneProblemType.DOCUMENTS_RECOVERY -> onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER)
                        PhoneProblemType.ARABIZATION -> showArabizationTool = true
                        PhoneProblemType.DATA_ACTIVATION_3G_4G -> onNavigateSection(ServiceSection.APN_SETTINGS)
                    }
                },
                onQuickAction = { code ->
                    AppIntentUtils.dialPhoneNumber(context, code)
                },
                onOpenSettings = { action ->
                    if (action.contains("LOCALE", true)) {
                        AppIntentUtils.openLocaleSettings(context)
                    } else {
                        AppIntentUtils.openApnSettings(context)
                    }
                }
            )
        }
    }

    // Detail dialog
    selectedDetailService?.let { service ->
        PhoneDetailDialog(
            service = service,
            onDismiss = { selectedDetailService = null },
            onRequestAssistance = {
                selectedDetailService = null
                serviceToRequest = service
            }
        )
    }

    // Request Service dialog
    serviceToRequest?.let { service ->
        RequestServiceDialog(
            initialCategory = "صيانة وبرمجة هواتف",
            initialSubCategory = service.title,
            onDismiss = { serviceToRequest = null },
            onSubmit = { title, category, subCategory, device, phone, notes ->
                onSubmitRequest(title, category, subCategory, device, phone, notes)
            }
        )
    }

    // Interactive Tools Dialogs
    if (showCarrierUnlockTool) {
        CarrierUnlockToolDialog(
            onDismiss = { showCarrierUnlockTool = false },
            onRequestDirectHelp = {
                showCarrierUnlockTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.CARRIER_UNLOCK }
            }
        )
    }

    if (showAccountRecoveryTool) {
        AccountRecoveryWizardDialog(
            onDismiss = { showAccountRecoveryTool = false },
            onRequestDirectHelp = {
                showAccountRecoveryTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.ACCOUNT_RECOVERY }
            }
        )
    }

    if (showArabizationTool) {
        ArabizationToolDialog(
            onDismiss = { showArabizationTool = false },
            onRequestDirectHelp = {
                showArabizationTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.ARABIZATION }
            }
        )
    }
}

@Composable
fun PhoneServiceCard(
    service: PhoneServiceDetail,
    onViewDetails: () -> Unit,
    onRequestService: () -> Unit,
    onLaunchInteractiveTool: () -> Unit,
    onQuickAction: (String) -> Unit,
    onOpenSettings: (String) -> Unit
) {
    val accentColor = getAccentColorForType(service.type)
    val accentContainer = getAccentContainerForType(service.type)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(accentContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconForProblemType(service.type),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.type.titleAr,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSlate,
                lineHeight = 20.sp
            )

            // Individual Service Pricing Badge (YER / SAR / USD)
            service.pricing?.let { pricing ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ImmersiveSurfaceVariant,
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "قيمة الاشتراك المنفصل:",
                                fontSize = 11.sp,
                                color = TextSlate
                            )
                            Text(
                                text = "${pricing.yer} YER",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldPrimary
                            )
                        }
                        Text(
                            text = "(${pricing.sar} SAR / ${pricing.usd} USD)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentTeal
                        )
                    }
                }
            }

            // Step summary teaser
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ImmersiveSurfaceVariant)
                    .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "أبرز الخطوات المعتمدة لدى رقيب:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                service.steps.take(2).forEach { step ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(15.dp).padding(top = 2.dp)
                        )
                        Text(
                            text = step,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            color = TextSlate,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // High-priority Interactive Tool Launch Button
            val (toolLabel, toolIcon) = when (service.type) {
                PhoneProblemType.LOCK_BYPASS -> "تشغيل محاكي فك وتذكير القفل (إثبات الملكية)" to Icons.Default.Key
                PhoneProblemType.FAILURE_LOCK_SIMULATOR -> "تشغيل محاكي الفشل والأقفال المتقدمة" to Icons.Default.Key
                PhoneProblemType.CARRIER_UNLOCK -> "تشغيل أداة فحص وتوليد كود فك الشفرة" to Icons.Default.LockOpen
                PhoneProblemType.ACCOUNT_RECOVERY -> "تشغيل معالج استرجاع الحسابات وفك الحظر" to Icons.Default.ManageAccounts
                PhoneProblemType.DATA_RECOVERY -> "تشغيل فاحص استرداد الصور والملفات" to Icons.Default.RestorePage
                PhoneProblemType.DOCUMENTS_RECOVERY -> "تشغيل فاحص استرجاع المستندات والوثائق" to Icons.Default.RestorePage
                PhoneProblemType.ARABIZATION -> "تشغيل أداة التعريب وأوامر ADB" to Icons.Default.Translate
                PhoneProblemType.DATA_ACTIVATION_3G_4G -> "تشغيل أداة ضبط وتفعيل 4G وAPN" to Icons.Default.NetworkCell
            }

            Button(
                onClick = onLaunchInteractiveTool,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = ImmersiveBg
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(toolIcon, contentDescription = null, modifier = Modifier.size(17.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(toolLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSlate)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("دليل الحل", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onRequestService,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("طلب الصيانة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Quick shortcuts if any
            if (!service.quickActionCode.isNullOrBlank() || !service.settingsAction.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!service.quickActionCode.isNullOrBlank()) {
                        OutlinedButton(
                            onClick = { onQuickAction(service.quickActionCode) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextSlate
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("كود: ${service.quickActionCode}", fontSize = 11.sp)
                        }
                    }
                    if (!service.settingsAction.isNullOrBlank()) {
                        OutlinedButton(
                            onClick = { onOpenSettings(service.settingsAction) },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextSlate
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(14.dp), tint = AccentTeal)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("فتح الضبط", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

fun getIconForProblemType(type: PhoneProblemType): ImageVector {
    return when (type) {
        PhoneProblemType.CARRIER_UNLOCK -> Icons.Default.LockOpen
        PhoneProblemType.LOCK_BYPASS -> Icons.Default.PhonelinkLock
        PhoneProblemType.FAILURE_LOCK_SIMULATOR -> Icons.Default.Key
        PhoneProblemType.ACCOUNT_RECOVERY -> Icons.Default.ManageAccounts
        PhoneProblemType.DATA_RECOVERY -> Icons.Default.RestorePage
        PhoneProblemType.DOCUMENTS_RECOVERY -> Icons.Default.RestorePage
        PhoneProblemType.ARABIZATION -> Icons.Default.Translate
        PhoneProblemType.DATA_ACTIVATION_3G_4G -> Icons.Default.NetworkCell
    }
}

fun getAccentColorForType(type: PhoneProblemType): Color {
    return when (type) {
        PhoneProblemType.CARRIER_UNLOCK -> AccentBlue
        PhoneProblemType.LOCK_BYPASS -> AccentOrange
        PhoneProblemType.FAILURE_LOCK_SIMULATOR -> AccentOrange
        PhoneProblemType.ACCOUNT_RECOVERY -> AccentPurple
        PhoneProblemType.DATA_RECOVERY -> AccentPink
        PhoneProblemType.DOCUMENTS_RECOVERY -> AccentTeal
        PhoneProblemType.ARABIZATION -> EmeraldPrimary
        PhoneProblemType.DATA_ACTIVATION_3G_4G -> AccentTeal
    }
}

fun getAccentContainerForType(type: PhoneProblemType): Color {
    return when (type) {
        PhoneProblemType.CARRIER_UNLOCK -> AccentBlueContainer
        PhoneProblemType.LOCK_BYPASS -> AccentOrangeContainer
        PhoneProblemType.FAILURE_LOCK_SIMULATOR -> AccentOrangeContainer
        PhoneProblemType.ACCOUNT_RECOVERY -> AccentPurpleContainer
        PhoneProblemType.DATA_RECOVERY -> AccentPinkContainer
        PhoneProblemType.DOCUMENTS_RECOVERY -> AccentTealContainer
        PhoneProblemType.ARABIZATION -> Color(0x2634D399)
        PhoneProblemType.DATA_ACTIVATION_3G_4G -> AccentTealContainer
    }
}

