package com.example.sdk.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import com.example.sdk.sdk.CalendarColorOverrides
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset

@Immutable
data class CalendarColors(
    val primary: androidx.compose.ui.graphics.Color,
    val primaryVariant: androidx.compose.ui.graphics.Color,

    val background: androidx.compose.ui.graphics.Color,
    val surface: androidx.compose.ui.graphics.Color,

    val textPrimary: androidx.compose.ui.graphics.Color,
    val textSecondary: androidx.compose.ui.graphics.Color,
    val textTertiary: androidx.compose.ui.graphics.Color,

    val borderLight: androidx.compose.ui.graphics.Color,
    val borderMedium: androidx.compose.ui.graphics.Color,

    val selectedBackground: androidx.compose.ui.graphics.Color,
    val selectedBorder: androidx.compose.ui.graphics.Color,

    val income: androidx.compose.ui.graphics.Color,
    val incomeVariant: androidx.compose.ui.graphics.Color,
    val expense: androidx.compose.ui.graphics.Color,
    val expenseVariant: androidx.compose.ui.graphics.Color,

    val addSheetAccent: androidx.compose.ui.graphics.Color,
    val addSheetAccentSecondary: androidx.compose.ui.graphics.Color,
    val addSheetCategoryChip: androidx.compose.ui.graphics.Color,
    val addSheetRecurringAccent: androidx.compose.ui.graphics.Color
)

private fun defaultCalendarColors(): CalendarColors {
    return CalendarColors(
        primary = GreenPrimary,
        primaryVariant = GreenPrimaryVariant,
        background = White,
        surface = White,
        textPrimary = Gray900,
        textSecondary = Gray500,
        textTertiary = Gray700,
        borderLight = Gray100,
        borderMedium = Gray300,
        selectedBackground = GreenPrimary.copy(alpha = 0.12f),
        selectedBorder = GreenPrimary,
        income = GreenIncome,
        incomeVariant = GreenIncomeVariant,
        expense = RedExpense,
        expenseVariant = RedExpenseVariant,
        addSheetAccent = GreenPrimary,
        addSheetAccentSecondary = GreenPrimaryVariant,
        addSheetCategoryChip = GreenPrimary.copy(alpha = 0.14f),
        addSheetRecurringAccent = GreenPrimary.copy(alpha = 0.12f)
    )
}

private fun oceanCalendarColors(): CalendarColors {
    return CalendarColors(
        primary = OceanPrimary,
        primaryVariant = OceanPrimaryVariant,
        background = White,
        surface = White,
        textPrimary = Gray900,
        textSecondary = Gray500,
        textTertiary = Gray700,
        borderLight = Gray100,
        borderMedium = Gray300,
        selectedBackground = OceanSelectedBackground,
        selectedBorder = OceanPrimary,
        income = OceanPrimary,
        incomeVariant = OceanPrimaryVariant,
        expense = OceanExpense,
        expenseVariant = OceanExpenseVariant,
        addSheetAccent = OceanPrimary,
        addSheetAccentSecondary = OceanPrimaryVariant,
        addSheetCategoryChip = OceanPrimary.copy(alpha = 0.14f),
        addSheetRecurringAccent = OceanPrimary.copy(alpha = 0.12f)
    )
}

private fun graphiteCalendarColors(): CalendarColors {
    return CalendarColors(
        primary = GraphitePrimary,
        primaryVariant = GraphitePrimaryVariant,
        background = GraphiteBackground,
        surface = GraphiteSurface,
        textPrimary = Gray900,
        textSecondary = Gray500,
        textTertiary = Gray700,
        borderLight = Gray100,
        borderMedium = Gray300,
        selectedBackground = GraphiteSelectedBackground,
        selectedBorder = GraphitePrimary,
        income = GraphitePrimary,
        incomeVariant = GraphitePrimaryVariant,
        expense = GraphiteExpense,
        expenseVariant = GraphiteExpenseVariant,
        addSheetAccent = GraphitePrimary,
        addSheetAccentSecondary = GraphitePrimaryVariant,
        addSheetCategoryChip = GraphitePrimary.copy(alpha = 0.16f),
        addSheetRecurringAccent = GraphitePrimary.copy(alpha = 0.14f)
    )
}

private fun CalendarColors.applyOverrides(overrides: CalendarColorOverrides): CalendarColors {
    return copy(
        primary = overrides.primary ?: primary,
        primaryVariant = overrides.primaryVariant ?: primaryVariant,
        background = overrides.background ?: background,
        surface = overrides.surface ?: surface,
        textPrimary = overrides.textPrimary ?: textPrimary,
        textSecondary = overrides.textSecondary ?: textSecondary,
        textTertiary = overrides.textTertiary ?: textTertiary,
        borderLight = overrides.borderLight ?: borderLight,
        borderMedium = overrides.borderMedium ?: borderMedium,
        selectedBackground = overrides.selectedBackground ?: selectedBackground,
        selectedBorder = overrides.selectedBorder ?: selectedBorder,
        income = overrides.income ?: income,
        incomeVariant = overrides.incomeVariant ?: incomeVariant,
        expense = overrides.expense ?: expense,
        expenseVariant = overrides.expenseVariant ?: expenseVariant,
        addSheetAccent = overrides.addSheetAccent ?: addSheetAccent,
        addSheetAccentSecondary = overrides.addSheetAccentSecondary ?: addSheetAccentSecondary,
        addSheetCategoryChip = overrides.addSheetCategoryChip ?: addSheetCategoryChip,
        addSheetRecurringAccent = overrides.addSheetRecurringAccent ?: addSheetRecurringAccent
    )
}

fun resolveCalendarColors(themeConfig: CalendarThemeConfig): CalendarColors {
    val base = when (themeConfig.preset) {
        CalendarThemePreset.Default -> defaultCalendarColors()
        CalendarThemePreset.Ocean -> oceanCalendarColors()
        CalendarThemePreset.Graphite -> graphiteCalendarColors()
    }

    return base.applyOverrides(themeConfig.colors)
}

val LocalCalendarColors = staticCompositionLocalOf { defaultCalendarColors() }

object CalendarTheme {
    val colors: CalendarColors
        @Composable get() = LocalCalendarColors.current

    val typography: CalendarTypography
        @Composable get() = LocalCalendarTypography.current

    val icons: CalendarIcons
        @Composable get() = LocalCalendarIcons.current
}

@Composable
fun FinancialCalendarTheme(
    themeConfig: CalendarThemeConfig,
    content: @Composable () -> Unit
) {
    val resolvedColors = remember(themeConfig) {
        resolveCalendarColors(themeConfig)
    }
    val resolvedTypography = remember(themeConfig) {
        resolveCalendarTypography(themeConfig)
    }
    val resolvedIcons = remember(themeConfig) {
        resolveCalendarIcons(themeConfig)
    }

    CompositionLocalProvider(
        LocalCalendarColors provides resolvedColors,
        LocalCalendarTypography provides resolvedTypography,
        LocalCalendarIcons provides resolvedIcons,
        content = content
    )
}