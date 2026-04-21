package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemeExamples

/**
 * Готовые demo-обёртки для быстрого запуска FinancialCalendarView
 * с разными конфигурациями темы внутри SDK-модуля.
 *
 * Эти composable можно использовать:
 * - для внутреннего тестирования,
 * - для demo-экранов,
 * - для быстрой проверки темы.
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarDefaultThemeDemo(
    viewModel: CalendarViewModel
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = CalendarThemeExamples.defaultTheme
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarOceanThemeDemo(
    viewModel: CalendarViewModel
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = CalendarThemeExamples.oceanTheme
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarGraphiteThemeDemo(
    viewModel: CalendarViewModel
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = CalendarThemeExamples.graphiteTheme
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarPartialOverrideThemeDemo(
    viewModel: CalendarViewModel
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = CalendarThemeExamples.partialOverrideTheme
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarFullOverrideThemeDemo(
    viewModel: CalendarViewModel
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = CalendarThemeExamples.fullOverrideTheme
    )
}

/**
 * Универсальная demo-обёртка, если нужно передать любую тему вручную.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialCalendarWithThemeDemo(
    viewModel: CalendarViewModel,
    themeConfig: CalendarThemeConfig
) {
    FinancialCalendarView(
        viewModel = viewModel,
        themeConfig = themeConfig
    )
}