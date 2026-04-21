package com.example.sdk.presentation.themeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun IconPickerBottomSheet(
    field: ThemeIconField,
    initialIconRes: Int?,
    onApply: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var selectedIconRes by remember {
        mutableIntStateOf(initialIconRes ?: ThemeIconFields.availableIcons.first().resId)
    }

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
                .size(56.dp)
                .background(colors.background, RoundedCornerShape(16.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = selectedIconRes,
                color = colors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        ThemeIconFields.availableIcons.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { option ->
                    val isSelected = option.resId == selectedIconRes

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (isSelected) colors.selectedBackground else colors.background,
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) colors.selectedBorder else colors.borderLight,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = { selectedIconRes = option.resId }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconWrapper(
                                    iconRes = option.resId,
                                    color = colors.textPrimary,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = option.title,
                                    style = typography.bodySmall,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))

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
                onClick = { onApply(selectedIconRes) },
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