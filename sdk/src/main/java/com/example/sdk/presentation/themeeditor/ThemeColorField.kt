package com.example.sdk.presentation.themeeditor

import androidx.compose.ui.graphics.Color
import com.example.sdk.sdk.CalendarColorOverrides

enum class ThemeColorFieldKey {
    Primary,
    PrimaryVariant,
    Background,
    Surface,
    TextPrimary,
    TextSecondary,
    TextTertiary,
    BorderLight,
    BorderMedium,
    SelectedBackground,
    SelectedBorder,
    Income,
    IncomeVariant,
    Expense,
    ExpenseVariant,
    AddSheetAccent,
    AddSheetAccentSecondary,
    AddSheetCategoryChip,
    AddSheetRecurringAccent
}

data class ThemeColorField(
    val key: ThemeColorFieldKey,
    val title: String,
    val subtitle: String? = null
)

object ThemeColorFields {

    val all: List<ThemeColorField> = listOf(
        ThemeColorField(
            key = ThemeColorFieldKey.Primary,
            title = "Основной акцент"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.PrimaryVariant,
            title = "Дополнительный акцент"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.Background,
            title = "Фон экрана"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.Surface,
            title = "Фон карточек"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.TextPrimary,
            title = "Основной текст"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.TextSecondary,
            title = "Вторичный текст"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.TextTertiary,
            title = "Дополнительный текст"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.BorderLight,
            title = "Светлая граница"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.BorderMedium,
            title = "Средняя граница"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.SelectedBackground,
            title = "Выделение фона"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.SelectedBorder,
            title = "Выделение границы"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.Income,
            title = "Цвет доходов"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.IncomeVariant,
            title = "Доп. цвет доходов"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.Expense,
            title = "Цвет расходов"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.ExpenseVariant,
            title = "Доп. цвет расходов"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.AddSheetAccent,
            title = "Акцент плюсика"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.AddSheetAccentSecondary,
            title = "Доп. акцент плюсика"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.AddSheetCategoryChip,
            title = "Фон иконок категорий"
        ),
        ThemeColorField(
            key = ThemeColorFieldKey.AddSheetRecurringAccent,
            title = "Фон recurring-блока"
        )
    )
}

fun CalendarColorOverrides.getColorValue(key: ThemeColorFieldKey): Color? {
    return when (key) {
        ThemeColorFieldKey.Primary -> primary
        ThemeColorFieldKey.PrimaryVariant -> primaryVariant
        ThemeColorFieldKey.Background -> background
        ThemeColorFieldKey.Surface -> surface
        ThemeColorFieldKey.TextPrimary -> textPrimary
        ThemeColorFieldKey.TextSecondary -> textSecondary
        ThemeColorFieldKey.TextTertiary -> textTertiary
        ThemeColorFieldKey.BorderLight -> borderLight
        ThemeColorFieldKey.BorderMedium -> borderMedium
        ThemeColorFieldKey.SelectedBackground -> selectedBackground
        ThemeColorFieldKey.SelectedBorder -> selectedBorder
        ThemeColorFieldKey.Income -> income
        ThemeColorFieldKey.IncomeVariant -> incomeVariant
        ThemeColorFieldKey.Expense -> expense
        ThemeColorFieldKey.ExpenseVariant -> expenseVariant
        ThemeColorFieldKey.AddSheetAccent -> addSheetAccent
        ThemeColorFieldKey.AddSheetAccentSecondary -> addSheetAccentSecondary
        ThemeColorFieldKey.AddSheetCategoryChip -> addSheetCategoryChip
        ThemeColorFieldKey.AddSheetRecurringAccent -> addSheetRecurringAccent
    }
}

fun CalendarColorOverrides.withColorValue(
    key: ThemeColorFieldKey,
    color: Color?
): CalendarColorOverrides {
    return when (key) {
        ThemeColorFieldKey.Primary -> copy(primary = color)
        ThemeColorFieldKey.PrimaryVariant -> copy(primaryVariant = color)
        ThemeColorFieldKey.Background -> copy(background = color)
        ThemeColorFieldKey.Surface -> copy(surface = color)
        ThemeColorFieldKey.TextPrimary -> copy(textPrimary = color)
        ThemeColorFieldKey.TextSecondary -> copy(textSecondary = color)
        ThemeColorFieldKey.TextTertiary -> copy(textTertiary = color)
        ThemeColorFieldKey.BorderLight -> copy(borderLight = color)
        ThemeColorFieldKey.BorderMedium -> copy(borderMedium = color)
        ThemeColorFieldKey.SelectedBackground -> copy(selectedBackground = color)
        ThemeColorFieldKey.SelectedBorder -> copy(selectedBorder = color)
        ThemeColorFieldKey.Income -> copy(income = color)
        ThemeColorFieldKey.IncomeVariant -> copy(incomeVariant = color)
        ThemeColorFieldKey.Expense -> copy(expense = color)
        ThemeColorFieldKey.ExpenseVariant -> copy(expenseVariant = color)
        ThemeColorFieldKey.AddSheetAccent -> copy(addSheetAccent = color)
        ThemeColorFieldKey.AddSheetAccentSecondary -> copy(addSheetAccentSecondary = color)
        ThemeColorFieldKey.AddSheetCategoryChip -> copy(addSheetCategoryChip = color)
        ThemeColorFieldKey.AddSheetRecurringAccent -> copy(addSheetRecurringAccent = color)
    }
}

fun Color.toHexArgbString(): String {
    val a = (alpha * 255).toInt().coerceIn(0, 255)
    val r = (red * 255).toInt().coerceIn(0, 255)
    val g = (green * 255).toInt().coerceIn(0, 255)
    val b = (blue * 255).toInt().coerceIn(0, 255)

    return String.format("#%02X%02X%02X%02X", a, r, g, b)
}

fun parseHexColorOrNull(rawValue: String): Color? {
    val normalized = rawValue
        .trim()
        .removePrefix("#")
        .uppercase()

    return when (normalized.length) {
        6 -> {
            val r = normalized.substring(0, 2).toIntOrNull(16) ?: return null
            val g = normalized.substring(2, 4).toIntOrNull(16) ?: return null
            val b = normalized.substring(4, 6).toIntOrNull(16) ?: return null

            Color(
                red = r / 255f,
                green = g / 255f,
                blue = b / 255f,
                alpha = 1f
            )
        }

        8 -> {
            val a = normalized.substring(0, 2).toIntOrNull(16) ?: return null
            val r = normalized.substring(2, 4).toIntOrNull(16) ?: return null
            val g = normalized.substring(4, 6).toIntOrNull(16) ?: return null
            val b = normalized.substring(6, 8).toIntOrNull(16) ?: return null

            Color(
                red = r / 255f,
                green = g / 255f,
                blue = b / 255f,
                alpha = a / 255f
            )
        }

        else -> null
    }
}