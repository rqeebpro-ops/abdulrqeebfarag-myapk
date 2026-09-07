package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ServiceRequestEntity
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPinkContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils

@Composable
fun MyRequestsScreen(
    requests: List<ServiceRequestEntity>,
    onDeleteRequest: (Long) -> Unit
) {
    val context = LocalContext.current

    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ImmersiveBg)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, ImmersiveBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(AccentPinkContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Assignment,
                            contentDescription = null,
                            tint = AccentPink,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Text(
                        text = "لا توجد طلبات مسجلة حتى الآن",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "عندما تقوم بطلب خدمة صيانة هاتف أو باقة تسويق ستظهر جميع طلباتك وحالتها فوراً هنا.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ImmersiveBg)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
        ) {
            item {
                Text(
                    text = "سجل طلباتي واستشاراتي (${requests.size}):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            items(requests, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                modifier = Modifier.weight(1f)
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0x3334D399),
                                border = BorderStroke(1.dp, Color(0x6634D399))
                            ) {
                                Text(
                                    text = item.status,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = "القسم: ${item.category} • الموديل/النشاط: ${item.deviceModelOrBusiness}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AccentTeal
                        )

                        if (item.notes.isNotEmpty()) {
                            Text(
                                text = "ملاحظات: ${item.notes}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSlate
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val msg = "مرحباً مركز رقيب، أود متابعة طلبي رقم #${item.id} بخصوص ${item.title}."
                                    AppIntentUtils.openWhatsApp(context, msg)
                                },
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, DarkButtonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = TextWhite
                                )
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("متابعة بالواتساب", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            IconButton(onClick = { onDeleteRequest(item.id) }) {
                                Icon(
                                    Icons.Default.DeleteOutline,
                                    contentDescription = "حذف الطلب",
                                    tint = AccentPink
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

