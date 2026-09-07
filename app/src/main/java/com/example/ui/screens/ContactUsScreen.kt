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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldLight
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

@Composable
fun ContactUsScreen() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
    ) {
        // Brand Profile Card
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(ImmersiveSurfaceVariant)
                            .border(1.dp, ImmersiveBorderSubtle, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.raqeeb_icon_1788464615035),
                            contentDescription = "شعار رقيب",
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Text(
                        text = "مركز رقيب للبرمجة والتسويق",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = "الوجهة الأولى للحلول البرمجية الشاملة للهواتف الذكية وحملات التسويق الرقمي وإدارة الهوية",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSlate
                    )

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0x3334D399),
                        border = BorderStroke(1.dp, Color(0x6634D399))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "خدمة موثوقة وسرية تامة لبيانات العملاء",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Contact Channels
        item {
            Text(
                text = "قنوات التواصل المباشر:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        // WhatsApp Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x2634D399)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "المحادثة عبر الواتساب",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "رد فوري ومباشر من فريق الدعم الفني",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate
                        )
                    }

                    Button(
                        onClick = {
                            AppIntentUtils.openWhatsApp(context, "مرحباً مركز رقيب، أود التواصل معكم.")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPrimary,
                            contentColor = ImmersiveBg
                        )
                    ) {
                        Text("مراسلة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Email Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AccentBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = AccentBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "البريد الإلكتروني الرسمي",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = AppIntentUtils.RAQEEB_EMAIL,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            color = AccentTeal
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            AppIntentUtils.sendEmail(
                                context,
                                "استفسار لمركز رقيب للبرمجة والتسويق",
                                "السلام عليكم ورحمة الله وبركاته،\n\nأود الاستفسار بخصوص..."
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkButtonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DarkButtonBg,
                            contentColor = TextWhite
                        )
                    ) {
                        Text("إرسال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Hotline Call Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AccentPurpleContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Call,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "الاتصال الهاتفي المباشر",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "خدمة العملاء والاستشارات العاجلة",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate
                        )
                    }

                    OutlinedButton(
                        onClick = { AppIntentUtils.dialPhoneNumber(context) },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkButtonBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DarkButtonBg,
                            contentColor = TextWhite
                        )
                    ) {
                        Text("اتصال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Official Payment Accounts Section
        item {
            Text(
                text = "الحسابات والمحافظ المالية الرسمية للدفع:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "يمكنك سداد قيمة الخدمات والاشتراكات (2000 ريال يمني أو ما يعادلها) عبر الحسابات التالية:",
                        fontSize = 12.sp,
                        color = TextSlate,
                        lineHeight = 18.sp
                    )

                    // Kuraimi Bank
                    PaymentAccountItem(
                        title = "بنك الكريمي للتمويل الأصغر",
                        accountNumber = AppIntentUtils.KURAMI_ACCOUNT,
                        accountType = "رقم الحساب المميز",
                        badge = "حساب بنكي",
                        badgeColor = EmeraldPrimary,
                        onCopy = {
                            AppIntentUtils.copyToClipboard(context, "حساب بنك الكريمي", AppIntentUtils.KURAMI_ACCOUNT)
                        }
                    )

                    // Jaib Wallet
                    PaymentAccountItem(
                        title = "محفظة جيب (Jeep)",
                        accountNumber = AppIntentUtils.JEEP_WALLET_PHONE,
                        accountType = "رقم المحفظة / الهاتف",
                        badge = "محفظة إلكترونية",
                        badgeColor = AccentTeal,
                        onCopy = {
                            AppIntentUtils.copyToClipboard(context, "محفظة جيب", AppIntentUtils.JEEP_WALLET_PHONE)
                        }
                    )

                    // One Cash Wallet
                    PaymentAccountItem(
                        title = "محفظة ون كاش (One Cash)",
                        accountNumber = AppIntentUtils.ONE_CASH_PHONE,
                        accountType = "رقم المحفظة / الهاتف",
                        badge = "محفظة إلكترونية",
                        badgeColor = AccentOrange,
                        onCopy = {
                            AppIntentUtils.copyToClipboard(context, "محفظة ون كاش", AppIntentUtils.ONE_CASH_PHONE)
                        }
                    )

                    // Jawwali Wallet
                    PaymentAccountItem(
                        title = "محفظة جوالي (Jawwali)",
                        accountNumber = AppIntentUtils.JAWWALI_PHONE,
                        accountType = "رقم المحفظة / الهاتف",
                        badge = "محفظة إلكترونية",
                        badgeColor = AccentPurple,
                        onCopy = {
                            AppIntentUtils.copyToClipboard(context, "محفظة جوالي", AppIntentUtils.JAWWALI_PHONE)
                        }
                    )
                }
            }
        }

        // Work hours & Security Guarantee
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                        Text(
                            text = "ساعات العمل: يومياً من 9:00 صباحاً حتى 10:00 مساءً",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(20.dp))
                        Text(
                            text = "الضمان والسرية: نلتزم بأعلى معايير الحفاظ على الخصوصية وعدم الاطلاع على ملفات وصور العميل أثناء الصيانة والبرمجة.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentAccountItem(
    title: String,
    accountNumber: String,
    accountType: String,
    badge: String,
    badgeColor: Color,
    onCopy: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ImmersiveSurfaceVariant,
        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = badgeColor.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = accountNumber,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "($accountType)",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            OutlinedButton(
                onClick = onCopy,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DarkButtonBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DarkButtonBg,
                    contentColor = TextWhite
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = EmeraldPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("نسخ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


