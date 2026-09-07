package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServiceSection
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate

@Composable
fun RaqeebBottomBar(
    currentSection: ServiceSection,
    onSectionSelected: (ServiceSection) -> Unit
) {
    NavigationBar(
        containerColor = ImmersiveBg,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = ImmersiveBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        val navItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = EmeraldPrimary,
            selectedTextColor = EmeraldPrimary,
            indicatorColor = EmeraldContainer,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted
        )

        val isPhoneSection = currentSection == ServiceSection.PHONE_SOLUTIONS || currentSection == ServiceSection.LOCK_RECOVERY_SIMULATOR
        NavigationBarItem(
            selected = isPhoneSection,
            onClick = { onSectionSelected(ServiceSection.PHONE_SOLUTIONS) },
            icon = { Icon(Icons.Default.PhoneAndroid, contentDescription = "برمجة الهواتف") },
            label = { Text("البرمجة", fontSize = 11.sp, fontWeight = if (isPhoneSection) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.MARKETING,
            onClick = { onSectionSelected(ServiceSection.MARKETING) },
            icon = { Icon(Icons.Default.Campaign, contentDescription = "التسويق") },
            label = { Text("التسويق", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.MARKETING) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.DATA_RECOVERY_SCANNER,
            onClick = { onSectionSelected(ServiceSection.DATA_RECOVERY_SCANNER) },
            icon = { Icon(Icons.Default.RestorePage, contentDescription = "استرداد البيانات") },
            label = { Text("الاسترداد", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.DATA_RECOVERY_SCANNER) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.APN_SETTINGS,
            onClick = { onSectionSelected(ServiceSection.APN_SETTINGS) },
            icon = { Icon(Icons.Default.CellTower, contentDescription = "تفعيل 4G") },
            label = { Text("بيانات 4G", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.APN_SETTINGS) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.MY_REQUESTS,
            onClick = { onSectionSelected(ServiceSection.MY_REQUESTS) },
            icon = { Icon(Icons.Default.Assignment, contentDescription = "طلباتي") },
            label = { Text("طلباتي", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.MY_REQUESTS) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.LOGIN_SUBSCRIPTION,
            onClick = { onSectionSelected(ServiceSection.LOGIN_SUBSCRIPTION) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "تسجيل واشتراك") },
            label = { Text("الاشتراك", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.LOGIN_SUBSCRIPTION) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.CONTACT_US,
            onClick = { onSectionSelected(ServiceSection.CONTACT_US) },
            icon = { Icon(Icons.Default.SupportAgent, contentDescription = "تواصل") },
            label = { Text("تواصل", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.CONTACT_US) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )

        NavigationBarItem(
            selected = currentSection == ServiceSection.OWNER_DATABASE,
            onClick = { onSectionSelected(ServiceSection.OWNER_DATABASE) },
            icon = { Icon(Icons.Default.Storage, contentDescription = "قاعدة المالك") },
            label = { Text("المالك", fontSize = 11.sp, fontWeight = if (currentSection == ServiceSection.OWNER_DATABASE) FontWeight.Bold else FontWeight.Normal) },
            colors = navItemColors
        )
    }
}

