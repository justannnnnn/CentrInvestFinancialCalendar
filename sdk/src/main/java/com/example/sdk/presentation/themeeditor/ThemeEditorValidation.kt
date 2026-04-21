package com.example.sdk.presentation.themeeditor

import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.sdk.CalendarThemePreset

const val THEME_NAME_MAX_LENGTH = 40

fun sanitizeThemeNameInput(input: String): String {
    return input.take(THEME_NAME_MAX_LENGTH)
}

fun resolveThemeNameForSave(
    rawName: String,
    mode: ThemeCustomizationMode,
    basePreset: CalendarThemePreset
): String {
    val trimmed = rawName.trim().take(THEME_NAME_MAX_LENGTH)

    if (trimmed.isNotBlank()) {
        return trimmed
    }

    return when (mode) {
        ThemeCustomizationMode.PARTIAL -> {
            when (basePreset) {
                CalendarThemePreset.Default -> "Моя тема"
                CalendarThemePreset.Ocean -> "Custom Ocean"
                CalendarThemePreset.Graphite -> "Custom Graphite"
            }
        }

        ThemeCustomizationMode.FULL -> {
            when (basePreset) {
                CalendarThemePreset.Default -> "Полная тема"
                CalendarThemePreset.Ocean -> "Full Ocean"
                CalendarThemePreset.Graphite -> "Full Graphite"
            }
        }
    }
}