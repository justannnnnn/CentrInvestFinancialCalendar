package com.example.sdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White

@Composable
fun ViewModeTabs(
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CalendarViewModeTab("grid", "Месяц", selectedMode == "month") { onModeSelected("month") }
        CalendarViewModeTab("week", "Неделя", selectedMode == "week") { onModeSelected("week") }
        CalendarViewModeTab("day", "День", selectedMode == "day") { onModeSelected("day") }
    }
}

@Composable
fun CalendarViewModeTab(
    iconType: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) GreenPrimary.copy(alpha = 0.12f) else Color.Transparent)
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) GreenPrimary else Gray100),
            contentAlignment = Alignment.Center
        ) {
            when (iconType) {
                "grid" -> MonthIcon(isSelected)
                "week" -> WeekIcon(isSelected)
                "day" -> DayIcon(isSelected)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) GreenPrimary else Gray500,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun MonthIcon(selected: Boolean) {
    val color = if (selected) White else Gray900.copy(alpha = 0.7f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
            }
        }
    }
}

@Composable
fun WeekIcon(selected: Boolean) {
    val color = if (selected) White else Gray900.copy(alpha = 0.5f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Row(modifier = Modifier.width(24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(3) {
                Box(modifier = Modifier.width(3.dp).height(12.dp).background(color, RoundedCornerShape(1.dp)))
            }
        }
    }
}

@Composable
fun DayIcon(selected: Boolean) {
    val color = if (selected) White else Gray900.copy(alpha = 0.5f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.width(10.dp).height(16.dp).background(color, RoundedCornerShape(2.dp)))
    }
}