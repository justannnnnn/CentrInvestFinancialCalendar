package com.example.sdk.presentation.models

import com.example.sdk.sdk.CalendarThemePreset

sealed class ThemePickerItem {

    data class PresetItem(
        val preset: CalendarThemePreset
    ) : ThemePickerItem()

    data class CustomThemeItem(
        val theme: CustomCalendarTheme
    ) : ThemePickerItem()

    data object CreatePartialThemeAction : ThemePickerItem()

    data object CreateFullThemeAction : ThemePickerItem()
}