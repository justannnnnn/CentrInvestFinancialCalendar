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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeSelection
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.GraphitePrimary
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.OceanPrimary

@Immutable
private data class ThemePreviewItem(
    val preset: CalendarThemePreset,
    val title: String,
    val subtitle: String,
    val previewColors: List<Color>
)

@Composable
fun ThemePickerBottomSheet(
    selectedThemeSelection: ThemeSelection,
    customThemes: List<CustomCalendarTheme>,
    onPresetSelected: (CalendarThemePreset) -> Unit,
    onCustomThemeSelected: (CustomCalendarTheme) -> Unit,
    onCreateTheme: () -> Unit,
    onEditCustomTheme: (CustomCalendarTheme) -> Unit,
    onDuplicateCustomTheme: (CustomCalendarTheme) -> Unit,
    onDeleteCustomTheme: (CustomCalendarTheme) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val themes = listOf(
        ThemePreviewItem(
            preset = CalendarThemePreset.Default,
            title = "Default",
            subtitle = "Базовая зелёная тема календаря",
            previewColors = listOf(
                GreenPrimary,
                Color.White,
                Color(0xFF111827)
            )
        ),
        ThemePreviewItem(
            preset = CalendarThemePreset.Ocean,
            title = "Ocean",
            subtitle = "Спокойная синяя цветовая схема",
            previewColors = listOf(
                OceanPrimary,
                Color.White,
                Color(0xFF111827)
            )
        ),
        ThemePreviewItem(
            preset = CalendarThemePreset.Graphite,
            title = "Graphite",
            subtitle = "Графитово-фиолетовая акцентная тема",
            previewColors = listOf(
                GraphitePrimary,
                Color.White,
                Color(0xFF111827)
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 42.dp, height = 4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colors.borderMedium)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Темы оформления",
            style = typography.titleLarge,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Готовые пресеты, ваши темы и действия по созданию новой темы",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        SectionTitle(text = "Готовые темы")

        Spacer(modifier = Modifier.height(12.dp))

        themes.forEach { item ->
            ThemePreviewCard(
                title = item.title,
                subtitle = item.subtitle,
                previewColors = item.previewColors,
                isSelected = selectedThemeSelection is ThemeSelection.Preset &&
                        selectedThemeSelection.preset == item.preset,
                onClick = { onPresetSelected(item.preset) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        SectionTitle(text = "Мои темы")

        Spacer(modifier = Modifier.height(12.dp))

        if (customThemes.isEmpty()) {
            EmptyCustomThemesCard()
        } else {
            customThemes.forEach { theme ->
                CustomThemeListItem(
                    theme = theme,
                    isSelected = selectedThemeSelection is ThemeSelection.Custom &&
                            selectedThemeSelection.themeId == theme.id,
                    onClick = { onCustomThemeSelected(theme) },
                    onEdit = { onEditCustomTheme(theme) },
                    onDuplicate = { onDuplicateCustomTheme(theme) },
                    onDelete = { onDeleteCustomTheme(theme) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        SectionTitle(text = "Действия")

        Spacer(modifier = Modifier.height(12.dp))

        ActionCard(
            title = "Создать новую тему",
            subtitle = "Выбор режима: частичная или полная настройка",
            onClick = onCreateTheme,
            enabled = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.borderLight)
                .clickable { onDismiss() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Закрыть",
                style = typography.bodyLarge,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Text(
        text = text,
        style = typography.titleMedium,
        color = colors.textPrimary
    )
}

@Composable
private fun EmptyCustomThemesCard() {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.background)
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Пока нет пользовательских тем",
                style = typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Создай первую частичную тему и она появится в этом списке",
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun ThemePreviewCard(
    title: String,
    subtitle: String,
    previewColors: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            previewColors.take(3).forEach { previewColor ->
                Box(
                    modifier = Modifier
                        .size(18.dp)
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
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.background)
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .then(
                if (enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = typography.titleMedium,
            color = if (enabled) colors.textPrimary else colors.textSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}