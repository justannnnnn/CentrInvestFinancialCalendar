package com.example.sdk.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.sdk.R
import com.example.sdk.sdk.CalendarIconOverrides
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset

@Immutable
data class CalendarIcons(
    @DrawableRes val add: Int,
    @DrawableRes val monthTab: Int,
    @DrawableRes val weekTab: Int,
    @DrawableRes val dayTab: Int,
    @DrawableRes val calendarBottomNav: Int,
    @DrawableRes val arrowLeft: Int,
    @DrawableRes val arrowRight: Int,
    @DrawableRes val recurring: Int
)

private fun defaultCalendarIcons(): CalendarIcons {
    return CalendarIcons(
        add = R.drawable.plus,
        monthTab = R.drawable.ic_month,
        weekTab = R.drawable.ic_week,
        dayTab = R.drawable.ic_day,
        calendarBottomNav = R.drawable.calendar,
        arrowLeft = R.drawable.chevron_left,
        arrowRight = R.drawable.chevron_right,
        recurring = R.drawable.calendar
    )
}

private fun oceanCalendarIcons(): CalendarIcons {
    return defaultCalendarIcons()
}

private fun graphiteCalendarIcons(): CalendarIcons {
    return defaultCalendarIcons()
}

private fun CalendarIcons.applyOverrides(
    overrides: CalendarIconOverrides
): CalendarIcons {
    return copy(
        add = overrides.addIconRes ?: add,
        monthTab = overrides.monthTabIconRes ?: monthTab,
        weekTab = overrides.weekTabIconRes ?: weekTab,
        dayTab = overrides.dayTabIconRes ?: dayTab,
        calendarBottomNav = overrides.calendarBottomNavIconRes ?: calendarBottomNav,
        arrowLeft = overrides.arrowLeftIconRes ?: arrowLeft,
        arrowRight = overrides.arrowRightIconRes ?: arrowRight,
        recurring = overrides.recurringIconRes ?: recurring
    )
}

fun resolveCalendarIcons(themeConfig: CalendarThemeConfig): CalendarIcons {
    val base = when (themeConfig.preset) {
        CalendarThemePreset.Default -> defaultCalendarIcons()
        CalendarThemePreset.Ocean -> oceanCalendarIcons()
        CalendarThemePreset.Graphite -> graphiteCalendarIcons()
    }
    return base.applyOverrides(themeConfig.icons)
}

val LocalCalendarIcons = staticCompositionLocalOf { defaultCalendarIcons() }