package com.example.sdk.sdk

import android.content.Context
import com.example.sdk.hosttheme.CalendarHostResolvedColors
import com.example.sdk.hosttheme.CalendarHostTheme
import com.example.sdk.hosttheme.CalendarHostThemeHints
import com.example.sdk.hosttheme.CalendarHostThemeManager

object CalendarSdk {

    lateinit var SUPABASE_ANON_KEY: String
        private set

    var calendarThemeConfig: CalendarThemeConfig = CalendarThemeConfig()
        private set

    private var eventListener: CalendarEventListener? = null

    val isInitialized: Boolean
        get() = ::SUPABASE_ANON_KEY.isInitialized

    fun init(context: Context, config: CalendarConfig) {
        SUPABASE_ANON_KEY = config.supabaseAnonKey
        calendarThemeConfig = config.theme
    }

    fun getThemeConfig(): CalendarThemeConfig {
        return calendarThemeConfig
    }

    fun updateTheme(themeConfig: CalendarThemeConfig) {
        calendarThemeConfig = themeConfig
    }

    fun setListener(listener: CalendarEventListener?) {
        eventListener = listener
    }

    fun getListener(): CalendarEventListener? {
        return eventListener
    }

    fun openDemo(context: Context) {
        // Demo entry point is not provided inside the SDK itself.
        // Host app should open its own screen and place FinancialCalendarView there.
    }

    /**
     * Возвращает host-friendly представление ТЕКУЩЕЙ темы,
     * которая сейчас находится в памяти SDK.
     *
     * Использовать, если host app хочет получить тему сразу после init(...)
     * или после updateTheme(...), без чтения сохранённого выбора пользователя.
     */
    fun exportCurrentHostTheme(): CalendarHostTheme {
        return CalendarHostThemeManager.getCurrentTheme()
    }

    /**
     * Возвращает host-friendly представление любой конкретной темы SDK.
     *
     * Использовать, если host app сам передаёт нужный CalendarThemeConfig
     * и хочет получить наружную resolved-модель темы.
     */
    fun exportTheme(themeConfig: CalendarThemeConfig): CalendarHostTheme {
        return CalendarHostThemeManager.getTheme(themeConfig)
    }

    /**
     * Возвращает host-friendly тему с учётом сохранённого выбора пользователя:
     * - preset
     * - custom theme
     *
     * Если сохранённого выбора нет, используется текущая глобальная тема SDK.
     */
    fun exportHostTheme(context: Context): CalendarHostTheme {
        return CalendarHostThemeManager.getPersistedOrCurrentTheme(context)
    }

    /**
     * То же самое, что exportHostTheme(context),
     * но позволяет явно передать fallback themeConfig.
     */
    fun exportHostTheme(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig
    ): CalendarHostTheme {
        return CalendarHostThemeManager.getPersistedOrCurrentTheme(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        )
    }

    /**
     * Удобный bridge-метод, если host app нужны только resolved colors.
     */
    fun exportHostColors(context: Context): CalendarHostResolvedColors {
        return CalendarHostThemeManager.getPersistedOrCurrentColors(context)
    }

    /**
     * Удобный bridge-метод, если host app нужны только resolved colors
     * с явным fallback.
     */
    fun exportHostColors(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig
    ): CalendarHostResolvedColors {
        return CalendarHostThemeManager.getPersistedOrCurrentColors(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        )
    }

    /**
     * Удобный bridge-метод, если host app нужны только hints:
     * статус-бар, navigation bar, светлый/тёмный фон.
     */
    fun exportHostHints(context: Context): CalendarHostThemeHints {
        return CalendarHostThemeManager.getPersistedOrCurrentHints(context)
    }

    /**
     * То же самое, но с явным fallback.
     */
    fun exportHostHints(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig
    ): CalendarHostThemeHints {
        return CalendarHostThemeManager.getPersistedOrCurrentHints(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        )
    }
}