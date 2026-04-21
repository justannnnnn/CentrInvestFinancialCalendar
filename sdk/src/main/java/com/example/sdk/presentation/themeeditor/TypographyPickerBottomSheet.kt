package com.example.sdk.presentation.themeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun TypographyPickerBottomSheet(
    field: ThemeTypographyField,
    initialStyle: TextStyle?,
    onApply: (TextStyle?) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var fontSize by remember { mutableFloatStateOf(initialStyle?.fontSize?.value ?: 16f) }
    var lineHeight by remember { mutableFloatStateOf(initialStyle?.lineHeight?.value ?: 22f) }
    var fontWeight by remember { mutableIntStateOf(initialStyle?.fontWeight?.weight ?: 400) }

    val previewStyle = TextStyle(
        fontSize = fontSize.sp,
        lineHeight = lineHeight.sp,
        fontWeight = FontWeight(fontWeight)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 42.dp, height = 4.dp)
                .background(colors.borderMedium, RoundedCornerShape(100.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = field.title,
            style = typography.titleLarge,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Text(
                text = field.sampleText,
                style = previewStyle,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TypographySliderBlock(
            title = "Размер текста",
            value = fontSize,
            valueRange = 10f..36f,
            onValueChange = { fontSize = it }
        )

        TypographySliderBlock(
            title = "Межстрочный интервал",
            value = lineHeight,
            valueRange = 12f..44f,
            onValueChange = { lineHeight = it }
        )

        TypographySliderBlock(
            title = "Вес шрифта",
            value = fontWeight.toFloat(),
            valueRange = 300f..800f,
            steps = 4,
            onValueChange = { value ->
                val rounded = (value / 100).toInt() * 100
                fontWeight = rounded.coerceIn(300, 800)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Отмена",
                    style = typography.bodyLarge,
                    color = colors.textSecondary
                )
            }

            TextButton(
                onClick = { onApply(null) },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Сбросить",
                    style = typography.bodyLarge,
                    color = colors.textPrimary
                )
            }

            Button(
                onClick = { onApply(previewStyle) },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Применить",
                    style = typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun TypographySliderBlock(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = typography.bodyMedium,
                color = colors.textPrimary
            )

            Text(
                text = value.toInt().toString(),
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )

        Spacer(modifier = Modifier.height(6.dp))
    }
}