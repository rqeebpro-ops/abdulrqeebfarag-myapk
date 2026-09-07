package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.model.MarketingPackage
import com.example.ui.dialogs.RequestServiceDialog
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
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
import kotlin.math.roundToInt

@Composable
fun MarketingScreen(
    packages: List<MarketingPackage>,
    marketingBudget: Float,
    onBudgetChange: (Float) -> Unit,
    onSubmitRequest: (title: String, category: String, subCategory: String, device: String, phone: String, notes: String) -> Unit
) {
    val context = LocalContext.current
    var selectedPackageToOrder by remember { mutableStateOf<MarketingPackage?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Marketing Hero Header with Immersive Emerald-Teal Gradient
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
                                    text = "باقات التسويق الرقمي",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "نتائج فورية",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldLight
                            )
                        }

                        Text(
                            text = "قسم التسويق الرقمي المتكامل",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "حملات إعلانية ممولة، تصاميم جرافيك وهوية بصرية، إدارة صفحات السوشيال ميديا وحملات واتساب بأعلى عائد استثماري.",
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            color = EmeraldLight.copy(alpha = 0.95f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    AppIntentUtils.openWhatsApp(
                                        context,
                                        "مرحباً مركز رقيب للتسويق، أود استشارة تسويقية حول نشاطي التجاري وتفاصيل الباقات المتاحة."
                                    )
                                },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0x33FFFFFF),
                                    contentColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color(0x66FFFFFF)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("استشارة تسويقية مجانية بالواتساب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Interactive ROI & Marketing Budget Calculator
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x2638BDF8)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Calculate,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "حاسبة توقعات ونتائج الحملات الإعلانية",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Text(
                        text = "حدد ميزانيتك الإعلانية التقديرية لرؤية مدى الوصول والمبيعات المتوقعة:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الميزانية الإعلانية المقترحة:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "$${marketingBudget.roundToInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }

                    Slider(
                        value = marketingBudget,
                        onValueChange = onBudgetChange,
                        valueRange = 10f..500f,
                        steps = 48,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldPrimary,
                            activeTrackColor = EmeraldPrimary,
                            inactiveTrackColor = ImmersiveSurfaceVariant
                        )
                    )

                    // Results boxes
                    val budget = marketingBudget.roundToInt()
                    val minReach = (budget * 850).coerceAtLeast(1000)
                    val maxReach = (budget * 1700).coerceAtLeast(2500)
                    val estimatedLeads = (budget * 4.5).roundToInt()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ImmersiveSurfaceVariant)
                                .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "الوصول المتوقع",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSlate
                                )
                                Text(
                                    text = "$minReach - $maxReach",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary
                                )
                                Text(
                                    text = "عميل مستهدف",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ImmersiveSurfaceVariant)
                                .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(16.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "العملاء المحتملون (Leads)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSlate
                                )
                                Text(
                                    text = "$estimatedLeads+ رسالة",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentTeal
                                )
                                Text(
                                    text = "معدل تحويل عالٍ",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            val msg = "مرحباً رقيب للتسويق، أود إطلاق حملة تسويقية ممولة بميزانية تقديرية $budget$ لنشاطي التجاري."
                            AppIntentUtils.openWhatsApp(context, msg)
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkButtonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DarkButtonBg,
                            contentColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("طلب إطلاق حملة بهذه الميزانية ($$budget)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section Title
        item {
            Text(
                text = "باقات وخدمات التسويق الرقمي المعتمدة:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        // Marketing Packages List
        items(packages) { pkg ->
            MarketingPackageCard(
                pkg = pkg,
                onOrderPackage = { selectedPackageToOrder = pkg },
                onContactWhatsApp = {
                    val msg = "مرحباً رقيب للتسويق، أرغب بالاستفسار عن باقة: ${pkg.titleAr}"
                    AppIntentUtils.openWhatsApp(context, msg)
                }
            )
        }
    }

    // Request Service Dialog
    selectedPackageToOrder?.let { pkg ->
        RequestServiceDialog(
            initialCategory = "تسويق إلكتروني",
            initialSubCategory = pkg.titleAr,
            onDismiss = { selectedPackageToOrder = null },
            onSubmit = { title, category, subCategory, device, phone, notes ->
                onSubmitRequest(title, category, subCategory, device, phone, notes)
            }
        )
    }
}

@Composable
fun MarketingPackageCard(
    pkg: MarketingPackage,
    onOrderPackage: () -> Unit,
    onContactWhatsApp: () -> Unit
) {
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
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(EmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getMarketingIcon(pkg.icon),
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = pkg.titleAr,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = pkg.estimatedReach,
                            style = MaterialTheme.typography.bodySmall,
                            color = AccentTeal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (pkg.popularTag) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33F59E0B),
                        border = BorderStroke(1.dp, Color(0x66F59E0B))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = AccentOrange,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "الأكثر طلباً",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AccentOrange
                            )
                        }
                    }
                }
            }

            Text(
                text = pkg.subtitleAr,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSlate,
                lineHeight = 20.sp
            )

            // Features checklist
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ImmersiveSurfaceVariant)
                    .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pkg.features.forEach { feature ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextWhite
                        )
                    }
                }
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onContactWhatsApp,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("استفسار واتساب", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onOrderPackage,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("طلب الباقة الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun getMarketingIcon(icon: String): ImageVector {
    return when (icon) {
        "campaign" -> Icons.Default.Campaign
        "hub" -> Icons.Default.Hub
        "palette" -> Icons.Default.Palette
        "chat" -> Icons.Default.Chat
        "language" -> Icons.Default.Language
        "analytics" -> Icons.Default.Analytics
        else -> Icons.Default.TrendingUp
    }
}

