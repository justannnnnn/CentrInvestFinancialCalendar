package com.example.sdk.presentation.models

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.sdk.R
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White

sealed class ViewModeTab(
    @StringRes val name: Int,
    val icon: @Composable (selected: Boolean) -> Unit
) {
    data object Month: ViewModeTab(
        name = R.string.month,
        icon = { selected -> MonthIcon(selected) }
    )

    data object Week: ViewModeTab(
        name = R.string.week,
        icon = { selected -> WeekIcon(selected) }
    )

    data object Day: ViewModeTab(
        name = R.string.day,
        icon = { selected -> DayIcon(selected) }
    )
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