package com.example.sdk.hosttheme

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset

data class CalendarHostTextStyle(
    val fontSizeSp: Float?,
    val lineHeightSp: Float?,
    val fontWeight: Int?
)

data class CalendarHostResolvedTypography(
    val titleLarge: CalendarHostTextStyle,
    val titleMedium: CalendarHostTextStyle,
    val bodyLarge: CalendarHostTextStyle,
    val bodyMedium: CalendarHostTextStyle,
    val bodySmall: CalendarHostTextStyle,
    val labelMedium: CalendarHostTextStyle,
    val labelSmall: CalendarHostTextStyle
)

data class CalendarHostResolvedIcons(
    @DrawableRes val add: Int,
    @DrawableRes val monthTab: Int,
    @DrawableRes val weekTab: Int,
    @DrawableRes val dayTab: Int,
    @DrawableRes val calendarBottomNav: Int,
    @DrawableRes val arrowLeft: Int,
    @DrawableRes val arrowRight: Int
)

data class CalendarHostResolvedColors(
    @ColorInt val primary: Int,
    @ColorInt val primaryVariant: Int,
    @ColorInt val background: Int,
    @ColorInt val surface: Int,
    @ColorInt val textPrimary: Int,
    @ColorInt val textSecondary: Int,
    @ColorInt val textTertiary: Int,
    @ColorInt val borderLight: Int,
    @ColorInt val borderMedium: Int,
    @ColorInt val selectedBackground: Int,
    @ColorInt val selectedBorder: Int,
    @ColorInt val income: Int,
    @ColorInt val incomeVariant: Int,
    @ColorInt val expense: Int,
    @ColorInt val expenseVariant: Int
)

data class CalendarHostThemeHints(
    val isLightBackground: Boolean,
    val isLightSurface: Boolean,
    val useDarkStatusBarIcons: Boolean,
    val useDarkNavigationBarIcons: Boolean
)

data class CalendarHostTheme(
    val sourceConfig: CalendarThemeConfig,
    val preset: CalendarThemePreset,
    val colors: CalendarHostResolvedColors,
    val typography: CalendarHostResolvedTypography,
    val icons: CalendarHostResolvedIcons,
    val hints: CalendarHostThemeHints
)