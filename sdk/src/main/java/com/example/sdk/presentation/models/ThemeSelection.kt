package com.example.sdk.presentation.models

import com.example.sdk.sdk.CalendarThemePreset

sealed class ThemeSelection {
    data class Preset(val preset: CalendarThemePreset) : ThemeSelection()
    data class Custom(val themeId: String) : ThemeSelection()
}