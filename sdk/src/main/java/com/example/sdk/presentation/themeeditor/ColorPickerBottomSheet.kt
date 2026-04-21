package com.example.sdk.presentation.themeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun ColorPickerBottomSheet(
    title: String,
    initialColor: Color?,
    onApply: (Color?) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var red by remember { mutableFloatStateOf(((initialColor?.red ?: 0f) * 255f).coerceIn(0f, 255f)) }
    var green by remember { mutableFloatStateOf(((initialColor?.green ?: 0f) * 255f).coerceIn(0f, 255f)) }
    var blue by remember { mutableFloatStateOf(((initialColor?.blue ?: 0f) * 255f).coerceIn(0f, 255f)) }
    var alpha by remember { mutableFloatStateOf(((initialColor?.alpha ?: 1f) * 255f).coerceIn(0f, 255f)) }

    var hexValue by remember {
        mutableStateOf(initialColor?.toHexArgbString() ?: "#FF00A86B")
    }

    var isHexError by remember { mutableStateOf(false) }

    fun currentColor(): Color {
        return Color(
            red = red / 255f,
            green = green / 255f,
            blue = blue / 255f,
            alpha = alpha / 255f
        )
    }

    LaunchedEffect(red, green, blue, alpha) {
        hexValue = currentColor().toHexArgbString()
        isHexError = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 42.dp, height = 4.dp)
                .background(colors.borderMedium, RoundedCornerShape(100.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = typography.titleLarge,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(currentColor(), CircleShape)
                    .border(1.dp, colors.borderLight, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = currentColor().toHexArgbString(),
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = hexValue,
            onValueChange = { value ->
                hexValue = value
                val parsed = parseHexColorOrNull(value)
                if (parsed != null) {
                    red = parsed.red * 255f
                    green = parsed.green * 255f
                    blue = parsed.blue * 255f
                    alpha = parsed.alpha * 255f
                    isHexError = false
                } else {
                    isHexError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isHexError,
            label = {
                Text(
                    text = "HEX",
                    style = typography.bodyMedium
                )
            },
            placeholder = {
                Text(
                    text = "#FF00A86B",
                    style = typography.bodyMedium
                )
            }
        )

        if (isHexError) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Используй формат #RRGGBB или #AARRGGBB",
                style = typography.bodySmall,
                color = colors.expense
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Быстрые цвета",
            style = typography.titleMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val quickColors = listOf(
            Color(0xFF00A86B),
            Color(0xFF0EA5E9),
            Color(0xFF7C3AED),
            Color(0xFFF95E5A),
            Color(0xFF111827),
            Color(0xFFFFFFFF),
            Color(0xFFF3F4F6),
            Color(0x1400A86B),
            Color(0x140EA5E9),
            Color(0x147C3AED)
        )

        quickColors.chunked(5).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { presetColor ->
                    QuickColorCircle(
                        color = presetColor,
                        onClick = {
                            red = presetColor.red * 255f
                            green = presetColor.green * 255f
                            blue = presetColor.blue * 255f
                            alpha = presetColor.alpha * 255f
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        ChannelSlider(
            title = "R",
            value = red,
            onValueChange = { red = it }
        )

        ChannelSlider(
            title = "G",
            value = green,
            onValueChange = { green = it }
        )

        ChannelSlider(
            title = "B",
            value = blue,
            onValueChange = { blue = it }
        )

        ChannelSlider(
            title = "A",
            value = alpha,
            onValueChange = { alpha = it }
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
                onClick = { onApply(currentColor()) },
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
private fun QuickColorCircle(
    color: Color,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color, CircleShape)
            .border(1.dp, colors.borderLight, CircleShape)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.matchParentSize()
        ) {}
    }
}

@Composable
private fun ChannelSlider(
    title: String,
    value: Float,
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
            valueRange = 0f..255f
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}