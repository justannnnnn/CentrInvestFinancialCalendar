package com.example.sdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun ThemeCreationModeSheet(
    onModeSelected: (ThemeCustomizationMode) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

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
                .background(colors.borderMedium, RoundedCornerShape(100.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Создание темы",
            style = typography.titleLarge,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Выбери режим настройки новой темы",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        ModeCard(
            title = "Частичная тема",
            subtitle = "Быстрая настройка цветов на базе выбранного preset",
            onClick = {
                onModeSelected(ThemeCustomizationMode.PARTIAL)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ModeCard(
            title = "Полная тема",
            subtitle = "Цвета, типографика и иконки с полным контролем",
            onClick = {
                onModeSelected(ThemeCustomizationMode.FULL)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.borderLight, RoundedCornerShape(16.dp))
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
private fun ModeCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background, RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
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
}