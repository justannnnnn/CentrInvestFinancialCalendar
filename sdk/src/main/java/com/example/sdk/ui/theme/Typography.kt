package com.example.sdk.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarTypographyOverrides

@Immutable
data class CalendarTypography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

private fun defaultCalendarTypography(): CalendarTypography {
    return CalendarTypography(
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    )
}

private fun oceanCalendarTypography(): CalendarTypography {
    return defaultCalendarTypography()
}

private fun graphiteCalendarTypography(): CalendarTypography {
    return defaultCalendarTypography()
}

private fun CalendarTypography.applyOverrides(
    overrides: CalendarTypographyOverrides
): CalendarTypography {
    return copy(
        titleLarge = titleLarge.merge(overrides.titleLarge ?: TextStyle.Default),
        titleMedium = titleMedium.merge(overrides.titleMedium ?: TextStyle.Default),
        bodyLarge = bodyLarge.merge(overrides.bodyLarge ?: TextStyle.Default),
        bodyMedium = bodyMedium.merge(overrides.bodyMedium ?: TextStyle.Default),
        bodySmall = bodySmall.merge(overrides.bodySmall ?: TextStyle.Default),
        labelMedium = labelMedium.merge(overrides.labelMedium ?: TextStyle.Default),
        labelSmall = labelSmall.merge(overrides.labelSmall ?: TextStyle.Default)
    )
}

fun resolveCalendarTypography(themeConfig: CalendarThemeConfig): CalendarTypography {
    val base = when (themeConfig.preset) {
        CalendarThemePreset.Default -> defaultCalendarTypography()
        CalendarThemePreset.Ocean -> oceanCalendarTypography()
        CalendarThemePreset.Graphite -> graphiteCalendarTypography()
    }

    return base.applyOverrides(themeConfig.typography)
}

val LocalCalendarTypography = staticCompositionLocalOf { defaultCalendarTypography() }