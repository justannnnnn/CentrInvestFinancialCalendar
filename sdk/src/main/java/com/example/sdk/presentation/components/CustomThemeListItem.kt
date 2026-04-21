package com.example.sdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.resolveCalendarColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomThemeListItem(
    theme: CustomCalendarTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val resolvedColors = resolveCalendarColors(theme.themeConfig)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) colors.selectedBackground else colors.background
            )
            .border(
                width = 1.dp,
                color = if (isSelected) colors.selectedBorder else colors.borderLight,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = theme.name,
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.size(8.dp))

                CustomThemeModeBadge(mode = theme.mode)
            }

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                text = "База: ${theme.basePreset.name}",
                style = typography.bodyMedium,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = "Изменена ${formatThemeUpdatedAt(theme.updatedAt)}",
                style = typography.bodySmall,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    resolvedColors.primary,
                    resolvedColors.surface,
                    resolvedColors.textPrimary
                ).forEach { previewColor ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(previewColor)
                            .border(
                                width = 1.dp,
                                color = colors.borderLight,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CustomThemeActionChip(
                    text = "Изменить",
                    onClick = onEdit
                )

                CustomThemeActionChip(
                    text = "Дублировать",
                    onClick = onDuplicate
                )

                CustomThemeActionChip(
                    text = "Удалить",
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun CustomThemeModeBadge(
    mode: ThemeCustomizationMode
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(colors.borderLight)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = when (mode) {
                ThemeCustomizationMode.PARTIAL -> "Частичная"
                ThemeCustomizationMode.FULL -> "Полная"
            },
            style = typography.bodySmall,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun CustomThemeActionChip(
    text: String,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = typography.bodySmall,
            color = colors.textPrimary
        )
    }
}

private fun formatThemeUpdatedAt(updatedAt: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - updatedAt
    val dayMs = 24L * 60L * 60L * 1000L

    return when {
        diff < dayMs -> "сегодня"
        diff < dayMs * 2 -> "вчера"
        else -> {
            val formatter = SimpleDateFormat("d MMM yyyy", Locale("ru"))
            formatter.format(Date(updatedAt))
        }
    }
}