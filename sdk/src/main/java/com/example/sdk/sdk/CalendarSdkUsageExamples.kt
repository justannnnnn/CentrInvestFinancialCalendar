package com.example.sdk.sdk

/**
 * Примеры использования SDK со стороны приложения, которое подключает библиотеку.
 *
 * Важно:
 * Эти примеры НЕ выполняются внутри sdk-модуля.
 * Они нужны как документация для интегратора.
 */
object CalendarSdkUsageExamples {

    /*
    // 1. Глобальная дефолтная тема
    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.defaultTheme
        )
    )
    */

    /*
    // 2. Глобальная Ocean тема
    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.oceanTheme
        )
    )
    */

    /*
    // 3. Глобальная Graphite тема
    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.graphiteTheme
        )
    )
    */

    /*
    // 4. Частичный override
    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.partialOverrideTheme
        )
    )
    */

    /*
    // 5. Полная кастомизация
    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.fullOverrideTheme
        )
    )
    */

    /*
    // Локальная тема на конкретный экран
    FinancialCalendarView(
        themeConfig = CalendarThemeExamples.oceanTheme
    )
    */
}