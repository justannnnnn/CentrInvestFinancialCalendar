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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White

@Composable
fun ViewModeTabs(
    tabs: List<ViewModeTab>,
    selectedMode: ViewModeTab,
    onModeSelected: (ViewModeTab) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            tabs.forEach { tab ->
                CalendarViewModeTab(
                    tab = tab,
                    isSelected = selectedMode == tab,
                    onClick = { onModeSelected(tab) }
                )
            }
        }
    }
}

@Composable
fun CalendarViewModeTab(
    tab: ViewModeTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() }
                .background(if (isSelected) GreenPrimary else Gray100),
            contentAlignment = Alignment.Center
        ) {
            tab.icon(isSelected)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(tab.name),
            fontSize = 12.sp,
            color = if (isSelected) GreenPrimary else Gray500,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}