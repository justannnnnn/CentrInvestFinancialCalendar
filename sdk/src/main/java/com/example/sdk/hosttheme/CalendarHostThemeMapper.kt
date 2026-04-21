package com.example.sdk.hosttheme

import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.ui.theme.resolveCalendarColors
import com.example.sdk.ui.theme.resolveCalendarIcons
import com.example.sdk.ui.theme.resolveCalendarTypography

internal object CalendarHostThemeMapper {

    fun map(themeConfig: CalendarThemeConfig): CalendarHostTheme {
        val resolvedColors = resolveCalendarColors(themeConfig)
        val resolvedTypography = resolveCalendarTypography(themeConfig)
        val resolvedIcons = resolveCalendarIcons(themeConfig)

        return CalendarHostTheme(
            sourceConfig = themeConfig,
            preset = themeConfig.preset,
            colors = CalendarHostResolvedColors(
                primary = resolvedColors.primary.toArgb(),
                primaryVariant = resolvedColors.primaryVariant.toArgb(),
                background = resolvedColors.background.toArgb(),
                surface = resolvedColors.surface.toArgb(),
                textPrimary = resolvedColors.textPrimary.toArgb(),
                textSecondary = resolvedColors.textSecondary.toArgb(),
                textTertiary = resolvedColors.textTertiary.toArgb(),
                borderLight = resolvedColors.borderLight.toArgb(),
                borderMedium = resolvedColors.borderMedium.toArgb(),
                selectedBackground = resolvedColors.selectedBackground.toArgb(),
                selectedBorder = resolvedColors.selectedBorder.toArgb(),
                income = resolvedColors.income.toArgb(),
                incomeVariant = resolvedColors.incomeVariant.toArgb(),
                expense = resolvedColors.expense.toArgb(),
                expenseVariant = resolvedColors.expenseVariant.toArgb()
            ),
            typography = CalendarHostResolvedTypography(
                titleLarge = resolvedTypography.titleLarge.toHostTextStyle(),
                titleMedium = resolvedTypography.titleMedium.toHostTextStyle(),
                bodyLarge = resolvedTypography.bodyLarge.toHostTextStyle(),
                bodyMedium = resolvedTypography.bodyMedium.toHostTextStyle(),
                bodySmall = resolvedTypography.bodySmall.toHostTextStyle(),
                labelMedium = resolvedTypography.labelMedium.toHostTextStyle(),
                labelSmall = resolvedTypography.labelSmall.toHostTextStyle()
            ),
            icons = CalendarHostResolvedIcons(
                add = resolvedIcons.add,
                monthTab = resolvedIcons.monthTab,
                weekTab = resolvedIcons.weekTab,
                dayTab = resolvedIcons.dayTab,
                calendarBottomNav = resolvedIcons.calendarBottomNav,
                arrowLeft = resolvedIcons.arrowLeft,
                arrowRight = resolvedIcons.arrowRight
            ),
            hints = CalendarHostThemeHints(
                isLightBackground = resolvedColors.background.luminance() > 0.5f,
                isLightSurface = resolvedColors.surface.luminance() > 0.5f,
                useDarkStatusBarIcons = resolvedColors.surface.luminance() > 0.5f,
                useDarkNavigationBarIcons = resolvedColors.background.luminance() > 0.5f
            )
        )
    }

    private fun TextStyle.toHostTextStyle(): CalendarHostTextStyle {
        return CalendarHostTextStyle(
            fontSizeSp = fontSize.takeIfSpecified(),
            lineHeightSp = lineHeight.takeIfSpecified(),
            fontWeight = fontWeight?.weight
        )
    }

    private fun TextUnit.takeIfSpecified(): Float? {
        return if (this == TextUnit.Unspecified) null else value
    }
}