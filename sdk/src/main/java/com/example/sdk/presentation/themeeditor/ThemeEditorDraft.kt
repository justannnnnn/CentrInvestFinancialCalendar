package com.example.sdk.presentation.themeeditor

import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.sdk.CalendarColorOverrides
import com.example.sdk.sdk.CalendarIconOverrides
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarTypographyOverrides

data class ThemeEditorDraft(
    val id: String?,
    val name: String,
    val mode: ThemeCustomizationMode,
    val basePreset: CalendarThemePreset,
    val colorOverrides: CalendarColorOverrides,
    val typographyOverrides: CalendarTypographyOverrides,
    val iconOverrides: CalendarIconOverrides
)

fun ThemeEditorDraft.toThemeConfig(): CalendarThemeConfig {
    return CalendarThemeConfig(
        preset = basePreset,
        colors = colorOverrides,
        typography = typographyOverrides,
        icons = iconOverrides
    )
}

fun CustomCalendarTheme.toEditorDraft(): ThemeEditorDraft {
    return ThemeEditorDraft(
        id = id,
        name = name,
        mode = mode,
        basePreset = basePreset,
        colorOverrides = themeConfig.colors,
        typographyOverrides = themeConfig.typography,
        iconOverrides = themeConfig.icons
    )
}