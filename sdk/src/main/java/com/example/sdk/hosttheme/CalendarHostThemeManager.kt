package com.example.sdk.hosttheme

import android.content.Context
import com.example.sdk.presentation.models.ThemeSelection
import com.example.sdk.sdk.CalendarSdk
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemeExamples
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarThemeStorage

object CalendarHostThemeManager {

    /**
     * Берёт текущую глобальную тему SDK из CalendarSdk.
     * Подходит, если host app хочет получить тему,
     * которая сейчас активна в памяти.
     */
    fun getCurrentTheme(): CalendarHostTheme {
        return CalendarHostThemeMapper.map(CalendarSdk.getThemeConfig())
    }

    /**
     * Явно маппит любую переданную конфигурацию темы SDK
     * в host-friendly формат.
     */
    fun getTheme(themeConfig: CalendarThemeConfig): CalendarHostTheme {
        return CalendarHostThemeMapper.map(themeConfig)
    }

    /**
     * Берёт тему с учётом сохранённого выбора пользователя:
     * - preset
     * - custom theme
     *
     * Если ничего не сохранено, используется fallbackThemeConfig.
     */
    fun getPersistedOrCurrentTheme(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig = CalendarSdk.getThemeConfig()
    ): CalendarHostTheme {
        val resolvedConfig = resolvePersistedThemeConfig(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        )

        return CalendarHostThemeMapper.map(resolvedConfig)
    }

    /**
     * Удобный метод, если host app хочет только цвета.
     */
    fun getPersistedOrCurrentColors(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig = CalendarSdk.getThemeConfig()
    ): CalendarHostResolvedColors {
        return getPersistedOrCurrentTheme(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        ).colors
    }

    /**
     * Удобный метод, если host app хочет только дополнительные подсказки.
     */
    fun getPersistedOrCurrentHints(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig = CalendarSdk.getThemeConfig()
    ): CalendarHostThemeHints {
        return getPersistedOrCurrentTheme(
            context = context,
            fallbackThemeConfig = fallbackThemeConfig
        ).hints
    }

    private fun resolvePersistedThemeConfig(
        context: Context,
        fallbackThemeConfig: CalendarThemeConfig
    ): CalendarThemeConfig {
        val savedSelection = CalendarThemeStorage.getThemeSelection(context)
            ?: return fallbackThemeConfig

        return when (savedSelection) {
            is ThemeSelection.Preset -> {
                when (savedSelection.preset) {
                    CalendarThemePreset.Default -> CalendarThemeExamples.defaultTheme
                    CalendarThemePreset.Ocean -> CalendarThemeExamples.oceanTheme
                    CalendarThemePreset.Graphite -> CalendarThemeExamples.graphiteTheme
                }
            }

            is ThemeSelection.Custom -> {
                CalendarThemeStorage.findCustomThemeById(context, savedSelection.themeId)?.themeConfig
                    ?: fallbackThemeConfig
            }
        }
    }
}