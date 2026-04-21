package com.example.sdk.hosttheme

import com.example.sdk.sdk.CalendarSdk

/**
 * Примеры того, как host app может забирать тему из SDK.
 *
 * Важно:
 * SDK ничего не "впрыскивает" автоматически в host app.
 * Host app сам вызывает публичный API библиотеки
 * и получает готовые resolved-данные темы.
 */
object CalendarHostThemeUsageExamples {

    /*
    ============================================================
    1. Host app подключил SDK как зависимость
    ============================================================

    implementation("your.group:calendar-sdk:version")
    */

    /*
    ============================================================
    2. Host app инициализирует SDK
    ============================================================

    CalendarSdk.init(
        context = applicationContext,
        config = CalendarConfig(
            supabaseAnonKey = "your_key",
            theme = CalendarThemeExamples.oceanTheme
        )
    )
    */

    /*
    ============================================================
    3. Host app получает ТЕКУЩУЮ тему из памяти SDK
    ============================================================

    val currentTheme = CalendarSdk.exportCurrentHostTheme()

    val primaryColor = currentTheme.colors.primary
    val surfaceColor = currentTheme.colors.surface
    val addIconRes = currentTheme.icons.add
    val titleFontSizeSp = currentTheme.typography.titleLarge.fontSizeSp
    */

    /*
    ============================================================
    4. Host app получает тему с учётом сохранённого выбора пользователя
    ============================================================

    val hostTheme = CalendarSdk.exportHostTheme(context)

    // пример:
    toolbar.setBackgroundColor(hostTheme.colors.primary)
    window.statusBarColor = hostTheme.colors.surface

    val useDarkIcons = hostTheme.hints.useDarkStatusBarIcons
    */

    /*
    ============================================================
    5. Host app может забрать только цвета
    ============================================================

    val colors = CalendarSdk.exportHostColors(context)

    toolbar.setBackgroundColor(colors.primary)
    rootView.setBackgroundColor(colors.background)
    */

    /*
    ============================================================
    6. Host app может забрать только hints
    ============================================================

    val hints = CalendarSdk.exportHostHints(context)

    // например:
    // controller.isAppearanceLightStatusBars = hints.useDarkStatusBarIcons
    */

    /*
    ============================================================
    7. Host app может передать конкретную тему вручную
    ============================================================

    val exported = CalendarSdk.exportTheme(CalendarThemeExamples.graphiteTheme)
    val primary = exported.colors.primary
    */
}