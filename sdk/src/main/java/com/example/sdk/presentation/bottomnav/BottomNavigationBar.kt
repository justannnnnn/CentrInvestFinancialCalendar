package com.example.sdk.presentation.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
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
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun BottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.borderLight)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                label = "Главная",
                icon = Icons.Default.Home,
                drawableIconRes = null,
                isSelected = selectedTab == "home"
            ) { onTabSelected("home") }

            BottomNavItem(
                label = "Переводы",
                icon = Icons.AutoMirrored.Filled.Send,
                drawableIconRes = null,
                isSelected = selectedTab == "transfers"
            ) { onTabSelected("transfers") }

            BottomNavItem(
                label = "Статистика",
                icon = Icons.Default.PieChart,
                drawableIconRes = null,
                isSelected = selectedTab == "stats"
            ) { onTabSelected("stats") }

            BottomNavItem(
                label = "Календарь",
                icon = null,
                drawableIconRes = icons.calendarBottomNav,
                isSelected = selectedTab == "calendar"
            ) { onTabSelected("calendar") }

            BottomNavItem(
                label = "Профиль",
                icon = Icons.Default.Person,
                drawableIconRes = null,
                isSelected = selectedTab == "profile"
            ) { onTabSelected("profile") }
        }
    }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector?,
    drawableIconRes: Int?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        when {
            icon != null -> {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) colors.primary else colors.textSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            drawableIconRes != null -> {
                IconWrapper(
                    modifier = Modifier.size(24.dp),
                    iconRes = drawableIconRes,
                    color = if (isSelected) colors.primary else colors.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = if (isSelected) {
                typography.labelSmall.copy(fontWeight = FontWeight.Medium)
            } else {
                typography.labelSmall.copy(fontWeight = FontWeight.Normal)
            },
            color = if (isSelected) colors.primary else colors.textSecondary
        )
    }
}