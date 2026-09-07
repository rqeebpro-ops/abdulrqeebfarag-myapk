package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScannedMediaItem
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
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TealGradientDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DataRecoveryScreen(
    isScanning: Boolean,
    scanProgress: Float,
    scannedMedia: List<ScannedMediaItem>,
    selectedIds: Set<Long>,
    restoredIds: Set<Long>,
    onStartScan: () -> Unit,
    onToggleSelect: (Long) -> Unit,
    onRestoreSelected: () -> Unit,
    onSubmitLabRequest: (title: String, category: String, subCategory: String, device: String, phone: String, notes: String) -> Unit
) {
    val context = LocalContext.current
    var selectedMediaTypeFilter by remember { mutableStateOf("ALL") } // ALL, IMAGE, VIDEO, DOC, AUDIO
    var showLabRequestDialog by remember { mutableStateOf(false) }

    val filteredList = remember(scannedMedia, selectedMediaTypeFilter) {
        when (selectedMediaTypeFilter) {
            "IMAGE" -> scannedMedia.filter { it.mimeType.startsWith("image") }
            "VIDEO" -> scannedMedia.filter { it.mimeType.startsWith("video") }
            "DOC" -> scannedMedia.filter { it.mimeType.contains("pdf") || it.mimeType.contains("doc") || it.mimeType.contains("text") }
            "AUDIO" -> scannedMedia.filter { it.mimeType.startsWith("audio") }
            else -> scannedMedia
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Hero Header with Immersive Emerald-Teal Gradient
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
                                    text = "محرك الاسترداد العميق",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "فحص آمن 100%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldLight
                            )
                        }

                        Text(
                            text = "فاحص واسترداد البيانات المفقودة",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "يقوم النظام بفحص قطاعات الذاكرة المؤقتة (Cache/Thumbnails) ومخازن الوسائط لاستخراج ومعاينة الصور ومقاطع الفيديو والمستندات المحذوفة وحفظها بأمان.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = EmeraldLight.copy(alpha = 0.95f)
                        )

                        // Scan Action Button
                        Button(
                            onClick = onStartScan,
                            enabled = !isScanning,
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x33FFFFFF),
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color(0x66FFFFFF)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isScanning) Icons.Default.Refresh else Icons.Default.RestorePage,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isScanning) "جاري الفحص العميق..." else if (scannedMedia.isEmpty()) "بدء الفحص واسترداد الملفات" else "إعادة الفحص الآن",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isScanning) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                LinearProgressIndicator(
                                    progress = { scanProgress },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                    color = EmeraldLight,
                                    trackColor = Color(0x33FFFFFF)
                                )
                                Text(
                                    text = "جاري مسح قطاعات الذاكرة: ${(scanProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldLight
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter chips for media types
        if (scannedMedia.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedMediaTypeFilter == "ALL",
                        onClick = { selectedMediaTypeFilter = "ALL" },
                        label = { Text("الكل (${scannedMedia.size})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (selectedMediaTypeFilter == "ALL") EmeraldPrimary else ImmersiveBorder),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ImmersiveSurfaceCard,
                            labelColor = TextSlate,
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = ImmersiveBg
                        )
                    )
                    FilterChip(
                        selected = selectedMediaTypeFilter == "IMAGE",
                        onClick = { selectedMediaTypeFilter = "IMAGE" },
                        label = { Text("صور (${scannedMedia.count { it.mimeType.startsWith("image") }})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (selectedMediaTypeFilter == "IMAGE") EmeraldPrimary else ImmersiveBorder),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ImmersiveSurfaceCard,
                            labelColor = TextSlate,
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = ImmersiveBg
                        )
                    )
                    FilterChip(
                        selected = selectedMediaTypeFilter == "VIDEO",
                        onClick = { selectedMediaTypeFilter = "VIDEO" },
                        label = { Text("فيديو (${scannedMedia.count { it.mimeType.startsWith("video") }})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (selectedMediaTypeFilter == "VIDEO") EmeraldPrimary else ImmersiveBorder),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ImmersiveSurfaceCard,
                            labelColor = TextSlate,
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = ImmersiveBg
                        )
                    )
                    FilterChip(
                        selected = selectedMediaTypeFilter == "DOC",
                        onClick = { selectedMediaTypeFilter = "DOC" },
                        label = { Text("مستندات", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (selectedMediaTypeFilter == "DOC") EmeraldPrimary else ImmersiveBorder),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = ImmersiveSurfaceCard,
                            labelColor = TextSlate,
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = ImmersiveBg
                        )
                    )
                }
            }

            // Restore Selected Action Bar
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "الملفات المكتشفة (${filteredList.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "محدد: ${selectedIds.size} ملف",
                                style = MaterialTheme.typography.bodySmall,
                                color = AccentTeal
                            )
                        }

                        if (selectedIds.isNotEmpty()) {
                            Button(
                                onClick = onRestoreSelected,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = ImmersiveBg
                                )
                            ) {
                                Icon(Icons.Default.SaveAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("استرداد المحدد (${selectedIds.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Empty state when not scanned
        if (!isScanning && scannedMedia.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(AccentPinkContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.RestorePage,
                                contentDescription = null,
                                tint = AccentPink,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = "لم يتم إجراء فحص حتى الآن",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "اضغط على زر 'بدء الفحص واسترداد الملفات' أعلاه للبحث عن الصور ومقاطع الفيديو المحذوفة.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate
                        )
                    }
                }
            }
        }

        // Scanned Items List
        items(filteredList) { item ->
            val isSelected = selectedIds.contains(item.id)
            val isRestored = restoredIds.contains(item.id)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) ImmersiveSurfaceVariant else ImmersiveSurfaceCard
                ),
                border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else ImmersiveBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleSelect(item.id) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { onToggleSelect(item.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldPrimary,
                            checkmarkColor = ImmersiveBg,
                            uncheckedColor = TextSlate
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(getMediaContainerColor(item.mimeType)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getMediaIcon(item.mimeType),
                            contentDescription = null,
                            tint = getMediaTint(item.mimeType),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite,
                            maxLines = 1
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = formatFileSize(item.sizeBytes),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSlate
                            )
                            Text(
                                text = formatDate(item.dateModified),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }

                    if (isRestored) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0x3334D399),
                            border = BorderStroke(1.dp, Color(0x6634D399))
                        ) {
                            Text(
                                text = "مسترجع ✓",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Deep Lab Recovery Callout
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x26F59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = AccentOrange,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = "خدمة الاسترداد المخبري المتقدم (للهواتف المعطوبة والفورمات)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                    Text(
                        text = "في حالات تلف المعالج، سقوط الهاتف في الماء، كسر اللوحة الأم، أو الفورمات الكامل، يتوفر لدى مركز رقيب أجهزة استخراج البيانات مباشرة من رقاقات الذاكرة (Chip-Off Forensic Recovery).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate,
                        lineHeight = 20.sp
                    )
                    OutlinedButton(
                        onClick = { showLabRequestDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkButtonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DarkButtonBg,
                            contentColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("طلب فحص مخبري متخصص لدى رقيب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showLabRequestDialog) {
        RequestServiceDialog(
            initialCategory = "صيانة وبرمجة هواتف",
            initialSubCategory = "استرداد بيانات مخبري متقدم",
            onDismiss = { showLabRequestDialog = false },
            onSubmit = { title, category, subCategory, device, phone, notes ->
                onSubmitLabRequest(title, category, subCategory, device, phone, notes)
                showLabRequestDialog = false
            }
        )
    }
}

fun getMediaIcon(mimeType: String): ImageVector {
    return when {
        mimeType.startsWith("image") -> Icons.Default.Image
        mimeType.startsWith("video") -> Icons.Default.Movie
        mimeType.startsWith("audio") -> Icons.Default.Audiotrack
        else -> Icons.Default.Description
    }
}

fun getMediaTint(mimeType: String): Color {
    return when {
        mimeType.startsWith("image") -> AccentPink
        mimeType.startsWith("video") -> AccentBlue
        mimeType.startsWith("audio") -> AccentPurple
        else -> AccentOrange
    }
}

fun getMediaContainerColor(mimeType: String): Color {
    return when {
        mimeType.startsWith("image") -> AccentPinkContainer
        mimeType.startsWith("video") -> AccentBlueContainer
        mimeType.startsWith("audio") -> AccentPurpleContainer
        else -> AccentOrangeContainer
    }
}

fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1.0 -> String.format(Locale.ENGLISH, "%.1f GB", gb)
        mb >= 1.0 -> String.format(Locale.ENGLISH, "%.1f MB", mb)
        else -> String.format(Locale.ENGLISH, "%.0f KB", kb)
    }
}

fun formatDate(timeMillis: Long): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return sdf.format(Date(timeMillis))
}

