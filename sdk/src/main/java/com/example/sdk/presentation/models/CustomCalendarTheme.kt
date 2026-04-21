package com.example.sdk.presentation.models

import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset

data class CustomCalendarTheme(
    val id: String,
    val name: String,
    val mode: ThemeCustomizationMode,
    val basePreset: CalendarThemePreset,
    val themeConfig: CalendarThemeConfig,
    val createdAt: Long,
    val updatedAt: Long
)