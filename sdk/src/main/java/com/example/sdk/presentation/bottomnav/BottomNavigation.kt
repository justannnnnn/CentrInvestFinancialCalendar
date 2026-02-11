package com.example.sdk.presentation.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomNavItem("Главная", Icons.Default.Home, selectedTab == "home") { onTabSelected("home") }
        BottomNavItem("Переводы", Icons.AutoMirrored.Filled.Send, selectedTab == "transfers") { onTabSelected("transfers") }
        BottomNavItem("Статистика", Icons.Default.PieChart, selectedTab == "stats") { onTabSelected("stats") }
        BottomNavItem("Календарь", null, selectedTab == "calendar") { onTabSelected("calendar") }
        BottomNavItem("Профиль", Icons.Default.Person, selectedTab == "profile") { onTabSelected("profile") }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) GreenPrimary else Gray500,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = "📅",
                fontSize = 24.sp,
                color = if (isSelected) GreenPrimary else Gray500
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) GreenPrimary else Gray500,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}