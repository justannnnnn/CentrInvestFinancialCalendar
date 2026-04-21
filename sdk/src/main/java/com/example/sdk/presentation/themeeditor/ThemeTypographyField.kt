package com.example.sdk.presentation.themeeditor

import androidx.compose.ui.text.TextStyle
import com.example.sdk.sdk.CalendarTypographyOverrides

enum class ThemeTypographyFieldKey {
    TitleLarge,
    TitleMedium,
    BodyLarge,
    BodyMedium,
    BodySmall,
    LabelMedium,
    LabelSmall
}

data class ThemeTypographyField(
    val key: ThemeTypographyFieldKey,
    val title: String,
    val sampleText: String
)

object ThemeTypographyFields {
    val all: List<ThemeTypographyField> = listOf(
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.TitleLarge,
            title = "Крупный заголовок",
            sampleText = "Финансовый календарь"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.TitleMedium,
            title = "Средний заголовок",
            sampleText = "Темы оформления"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.BodyLarge,
            title = "Крупный основной текст",
            sampleText = "Апрель 2026"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.BodyMedium,
            title = "Основной текст",
            sampleText = "Категория и описание операции"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.BodySmall,
            title = "Мелкий текст",
            sampleText = "Подпись и вторичная информация"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.LabelMedium,
            title = "Средняя метка",
            sampleText = "Доходы"
        ),
        ThemeTypographyField(
            key = ThemeTypographyFieldKey.LabelSmall,
            title = "Мелкая метка",
            sampleText = "Month"
        )
    )
}

fun CalendarTypographyOverrides.getTextStyleValue(
    key: ThemeTypographyFieldKey
): TextStyle? {
    return when (key) {
        ThemeTypographyFieldKey.TitleLarge -> titleLarge
        ThemeTypographyFieldKey.TitleMedium -> titleMedium
        ThemeTypographyFieldKey.BodyLarge -> bodyLarge
        ThemeTypographyFieldKey.BodyMedium -> bodyMedium
        ThemeTypographyFieldKey.BodySmall -> bodySmall
        ThemeTypographyFieldKey.LabelMedium -> labelMedium
        ThemeTypographyFieldKey.LabelSmall -> labelSmall
    }
}

fun CalendarTypographyOverrides.withTextStyleValue(
    key: ThemeTypographyFieldKey,
    textStyle: TextStyle?
): CalendarTypographyOverrides {
    return when (key) {
        ThemeTypographyFieldKey.TitleLarge -> copy(titleLarge = textStyle)
        ThemeTypographyFieldKey.TitleMedium -> copy(titleMedium = textStyle)
        ThemeTypographyFieldKey.BodyLarge -> copy(bodyLarge = textStyle)
        ThemeTypographyFieldKey.BodyMedium -> copy(bodyMedium = textStyle)
        ThemeTypographyFieldKey.BodySmall -> copy(bodySmall = textStyle)
        ThemeTypographyFieldKey.LabelMedium -> copy(labelMedium = textStyle)
        ThemeTypographyFieldKey.LabelSmall -> copy(labelSmall = textStyle)
    }
}