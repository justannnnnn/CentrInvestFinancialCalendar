package com.example.sdk.sdk

import androidx.compose.ui.graphics.Color
import androidx.annotation.DrawableRes
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

/**
 * Готовые preset-темы SDK.
 *
 * Default  - базовая банковская зелёная тема
 * Ocean    - синяя тема
 * Graphite - графитово-фиолетовая тема
 */
enum class CalendarThemePreset {
    Default,
    Ocean,
    Graphite
}

/**
 * Частичные переопределения цветов темы.
 *
 * Любое поле можно не передавать — тогда будет использовано
 * значение из выбранного preset.
 *
 * Это позволяет поддерживать три сценария:
 * 1) использовать только preset,
 * 2) использовать preset + несколько своих цветов,
 * 3) собрать почти полностью свою тему на базе overrides.
 *
 * В color overrides включены только те цвета, которые реально
 * влияют на визуальную тему SDK. Layout-параметры сюда не входят.
 */
data class CalendarColorOverrides(
    val primary: Color? = null,
    val primaryVariant: Color? = null,

    val background: Color? = null,
    val surface: Color? = null,

    val textPrimary: Color? = null,
    val textSecondary: Color? = null,
    val textTertiary: Color? = null,

    val borderLight: Color? = null,
    val borderMedium: Color? = null,

    val selectedBackground: Color? = null,
    val selectedBorder: Color? = null,

    val income: Color? = null,
    val incomeVariant: Color? = null,

    val expense: Color? = null,
    val expenseVariant: Color? = null,

    val addSheetAccent: Color? = null,
    val addSheetAccentSecondary: Color? = null,
    val addSheetCategoryChip: Color? = null,
    val addSheetRecurringAccent: Color? = null
)


/**
 * Частичные переопределения текстовых стилей темы.
 *
 * Если конкретный стиль не передан, используется стиль из preset.
 * Typography отвечает только за текстовое оформление и не управляет
 * размерами/отступами layout-компонентов.
 */
data class CalendarTypographyOverrides(
    val titleLarge: TextStyle? = null,
    val titleMedium: TextStyle? = null,
    val bodyLarge: TextStyle? = null,
    val bodyMedium: TextStyle? = null,
    val bodySmall: TextStyle? = null,
    val labelMedium: TextStyle? = null,
    val labelSmall: TextStyle? = null
)


/**
 * Частичные переопределения иконок темы.
 *
 * Позволяет заменить ключевые иконки SDK без изменения внутреннего UI-кода.
 * Если конкретная иконка не передана, используется значение из preset.
 */
data class CalendarIconOverrides(
    @DrawableRes val addIconRes: Int? = null,
    @DrawableRes val monthTabIconRes: Int? = null,
    @DrawableRes val weekTabIconRes: Int? = null,
    @DrawableRes val dayTabIconRes: Int? = null,
    @DrawableRes val calendarBottomNavIconRes: Int? = null,
    @DrawableRes val arrowLeftIconRes: Int? = null,
    @DrawableRes val arrowRightIconRes: Int? = null,
    @DrawableRes val recurringIconRes: Int? = null
)

/**
 * Единая конфигурация темы SDK.
 *
 * Схема работы:
 * 1. Выбирается базовый preset.
 * 2. Поверх preset накладываются пользовательские overrides.
 * 3. На выходе формируется итоговая тема:
 *    - colors
 *    - typography
 *    - icons
 *
 * В публичную кастомизацию темы входят:
 * - цвета
 * - текстовые стили
 * - иконки
 *
 * В тему намеренно НЕ входят:
 * - размеры компонентов
 * - отступы
 * - радиусы
 * - анимации
 * - бизнес-логика календаря
 *
 * Это позволяет сохранить разумную степень кастомизации
 * и не превращать theme config в конфиг всего UI.
 */
data class CalendarThemeConfig(
    val preset: CalendarThemePreset = CalendarThemePreset.Default,
    val colors: CalendarColorOverrides = CalendarColorOverrides(),
    val typography: CalendarTypographyOverrides = CalendarTypographyOverrides(),
    val icons: CalendarIconOverrides = CalendarIconOverrides()
)


/**
 * Общая конфигурация SDK.
 *
 * theme задаёт глобальную тему библиотеки по умолчанию.
 * Она применяется ко всем экранам SDK после вызова CalendarSdk.init(...),
 * если конкретный экран не передал свой локальный themeConfig.
 *
 * Примеры:
 *
 * Default:
 * CalendarConfig(
 *     supabaseAnonKey = "...",
 *     theme = CalendarThemeConfig()
 * )
 *
 * Ocean:
 * CalendarConfig(
 *     supabaseAnonKey = "...",
 *     theme = CalendarThemeConfig(
 *         preset = CalendarThemePreset.Ocean
 *     )
 * )
 *
 * Partial override:
 * CalendarConfig(
 *     supabaseAnonKey = "...",
 *     theme = CalendarThemeConfig(
 *         preset = CalendarThemePreset.Default,
 *         colors = CalendarColorOverrides(
 *             primary = Color(0xFF1E88E5),
 *             selectedBackground = Color(0x141E88E5),
 *             income = Color(0xFF43A047),
 *             expense = Color(0xFFE53935)
 *         )
 *     )
 * )
 */
data class CalendarConfig(
    val supabaseAnonKey: String,
    val baseUrl: String? = null,  // для API
    val enableAnalytics: Boolean = true,
    val theme: CalendarThemeConfig = CalendarThemeConfig()
)