package com.example.sdk.sdk

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sdk.R

/**
 * Готовые примеры конфигурации темы SDK.
 *
 * Этот файл можно использовать как:
 * 1. внутреннюю документацию SDK,
 * 2. набор шаблонов для интегратора,
 * 3. быстрые пресеты для demo/debug.
 */
object CalendarThemeExamples {

    /**
     * 1. Дефолтная тема
     *
     * Использует стандартный preset Default без дополнительных overrides.
     */
    val defaultTheme: CalendarThemeConfig = CalendarThemeConfig()

    /**
     * 2. Готовый preset Ocean
     */
    val oceanTheme: CalendarThemeConfig = CalendarThemeConfig(
        preset = CalendarThemePreset.Ocean
    )

    /**
     * 3. Готовый preset Graphite
     */
    val graphiteTheme: CalendarThemeConfig = CalendarThemeConfig(
        preset = CalendarThemePreset.Graphite
    )

    /**
     * 4. Частичное переопределение цветов
     *
     * Используется базовый Default preset,
     * но часть цветов заменяется на пользовательские.
     */
    val partialOverrideTheme: CalendarThemeConfig = CalendarThemeConfig(
        preset = CalendarThemePreset.Default,
        colors = CalendarColorOverrides(
            primary = Color(0xFF1E88E5),
            selectedBackground = Color(0x141E88E5),
            income = Color(0xFF43A047),
            expense = Color(0xFFE53935)
        )
    )

    /**
     * 5. Полная кастомизация темы
     *
     * Здесь показан пример:
     * - своих цветов
     * - своей типографики
     * - своих иконок
     */
    val fullOverrideTheme: CalendarThemeConfig = CalendarThemeConfig(
        preset = CalendarThemePreset.Default,
        colors = CalendarColorOverrides(
            primary = Color(0xFF5E35B1),
            primaryVariant = Color(0xFF7E57C2),

            background = Color(0xFFF8F7FC),
            surface = Color(0xFFFFFFFF),

            textPrimary = Color(0xFF1A1A1A),
            textSecondary = Color(0xFF6B7280),
            textTertiary = Color(0xFF9CA3AF),

            borderLight = Color(0xFFE5E7EB),
            borderMedium = Color(0xFFD1D5DB),

            selectedBackground = Color(0x145E35B1),
            selectedBorder = Color(0xFF5E35B1),

            income = Color(0xFF2E7D32),
            incomeVariant = Color(0xFF43A047),

            expense = Color(0xFFC62828),
            expenseVariant = Color(0xFFE53935)
        ),
        typography = CalendarTypographyOverrides(
            titleLarge = TextStyle(
                fontSize = 22.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            bodyLarge = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            ),
            bodyMedium = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            ),
            bodySmall = TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            labelMedium = TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium
            ),
            labelSmall = TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )
        ),
        icons = CalendarIconOverrides(
            addIconRes = R.drawable.plus,
            monthTabIconRes = R.drawable.ic_month,
            weekTabIconRes = R.drawable.ic_week,
            dayTabIconRes = R.drawable.ic_day,
            calendarBottomNavIconRes = R.drawable.calendar,
            arrowLeftIconRes = R.drawable.chevron_left,
            arrowRightIconRes = R.drawable.chevron_right
        )
    )
}