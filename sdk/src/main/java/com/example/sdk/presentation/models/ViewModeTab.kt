package com.example.sdk.presentation.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.example.sdk.R
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme

sealed class ViewModeTab(
    @StringRes val name: Int,
    val icon: @Composable (selected: Boolean) -> Unit
) {
    data object Month : ViewModeTab(
        name = R.string.month,
        icon = { selected -> ThemeTabIcon(TabIconType.Month, selected) }
    )

    data object Week : ViewModeTab(
        name = R.string.week,
        icon = { selected -> ThemeTabIcon(TabIconType.Week, selected) }
    )

    data object Day : ViewModeTab(
        name = R.string.day,
        icon = { selected -> ThemeTabIcon(TabIconType.Day, selected) }
    )
}

private enum class TabIconType {
    Month, Week, Day
}

@Composable
private fun ThemeTabIcon(
    type: TabIconType,
    selected: Boolean
) {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    val iconRes = when (type) {
        TabIconType.Month -> icons.monthTab
        TabIconType.Week -> icons.weekTab
        TabIconType.Day -> icons.dayTab
    }

    IconWrapper(
        modifier = Modifier.size(20.dp),
        iconRes = iconRes,
        color = if (selected) colors.primary else colors.textPrimary.copy(alpha = 0.7f)
    )
}