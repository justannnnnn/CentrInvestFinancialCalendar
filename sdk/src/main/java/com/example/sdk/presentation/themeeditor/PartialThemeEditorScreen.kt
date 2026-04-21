package com.example.sdk.presentation.themeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.sdk.CalendarColorOverrides
import com.example.sdk.sdk.CalendarIconOverrides
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarTypographyOverrides
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.FinancialCalendarTheme
import com.example.sdk.ui.theme.resolveCalendarColors
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialThemeEditorScreen(
    initialTheme: CustomCalendarTheme? = null,
    defaultBasePreset: CalendarThemePreset = CalendarThemePreset.Default,
    editorThemeConfig: com.example.sdk.sdk.CalendarThemeConfig,
    onDismiss: () -> Unit,
    onRequestDismiss: (hasUnsavedChanges: Boolean) -> Unit,
    onSave: (CustomCalendarTheme) -> Unit
) {
    var draft by remember(initialTheme, defaultBasePreset) {
        mutableStateOf(
            initialTheme?.toEditorDraft() ?: ThemeEditorDraft(
                id = null,
                name = "",
                mode = ThemeCustomizationMode.PARTIAL,
                basePreset = defaultBasePreset,
                colorOverrides = CalendarColorOverrides(),
                typographyOverrides = CalendarTypographyOverrides(),
                iconOverrides = CalendarIconOverrides()
            )
        )
    }

    val initialDraft = remember(initialTheme, defaultBasePreset) {
        initialTheme?.toEditorDraft() ?: ThemeEditorDraft(
            id = null,
            name = "",
            mode = ThemeCustomizationMode.PARTIAL,
            basePreset = defaultBasePreset,
            colorOverrides = CalendarColorOverrides(),
            typographyOverrides = CalendarTypographyOverrides(),
            iconOverrides = CalendarIconOverrides()
        )
    }

    val hasUnsavedChanges = draft != initialDraft
    var activeColorField by remember { mutableStateOf<ThemeColorField?>(null) }

    FinancialCalendarTheme(themeConfig = editorThemeConfig) {
        val colors = CalendarTheme.colors
        val typography = CalendarTheme.typography

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(colors.surface)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            PartialEditorHandle()

            Spacer(modifier = Modifier.height(16.dp))

            PartialEditorTopBar(
                title = if (initialTheme == null) {
                    "Новая частичная тема"
                } else {
                    "Редактирование темы"
                },
                hasUnsavedChanges = hasUnsavedChanges,
                onBackClick = { onRequestDismiss(hasUnsavedChanges) },
                onSaveClick = {
                    onSave(buildPartialSavedTheme(initialTheme, draft))
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            PartialEditorSectionTitle(text = "Название темы")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = draft.name,
                onValueChange = { value ->
                    draft = draft.copy(
                        name = sanitizeThemeNameInput(value)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Например, Моя зелёная тема",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            PartialEditorSectionTitle(text = "Базовый preset")

            Spacer(modifier = Modifier.height(10.dp))

            PartialThemePresetSelectorRow(
                selectedPreset = draft.basePreset,
                onPresetSelected = { selected ->
                    draft = draft.copy(basePreset = selected)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            PartialEditorSectionTitle(text = "Превью")

            Spacer(modifier = Modifier.height(10.dp))

            ThemePreviewCard(draft = draft)

            Spacer(modifier = Modifier.height(20.dp))

            PartialEditorSectionTitle(text = "Цвета")

            Spacer(modifier = Modifier.height(12.dp))

            ThemeColorFields.all.forEach { field ->
                PartialEditorColorRow(
                    field = field,
                    resolvedColor = draft.colorOverrides.getColorValue(field.key)
                        ?: resolveBaseColorForPartialField(draft, field.key),
                    isOverridden = draft.colorOverrides.getColorValue(field.key) != null,
                    onClick = { activeColorField = field }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = {
                        draft = draft.copy(
                            colorOverrides = CalendarColorOverrides()
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Сбросить цвета",
                        style = typography.bodyLarge,
                        color = colors.textPrimary
                    )
                }

                Button(
                    onClick = {
                        onSave(buildPartialSavedTheme(initialTheme, draft))
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Сохранить",
                        style = typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { onRequestDismiss(hasUnsavedChanges) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Отмена",
                    style = typography.bodyLarge,
                    color = colors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (activeColorField != null) {
            val field = activeColorField!!

            ModalBottomSheet(
                onDismissRequest = { activeColorField = null },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                containerColor = colors.surface
            ) {
                ColorPickerBottomSheet(
                    title = field.title,
                    initialColor = draft.colorOverrides.getColorValue(field.key),
                    onApply = { selectedColor ->
                        draft = draft.copy(
                            colorOverrides = draft.colorOverrides.withColorValue(field.key, selectedColor)
                        )
                        activeColorField = null
                    },
                    onDismiss = {
                        activeColorField = null
                    }
                )
            }
        }
    }
}

private fun buildPartialSavedTheme(
    initialTheme: CustomCalendarTheme?,
    draft: ThemeEditorDraft
): CustomCalendarTheme {
    val now = System.currentTimeMillis()

    return CustomCalendarTheme(
        id = draft.id ?: UUID.randomUUID().toString(),
        name = resolveThemeNameForSave(
            rawName = draft.name,
            mode = ThemeCustomizationMode.PARTIAL,
            basePreset = draft.basePreset
        ),
        mode = ThemeCustomizationMode.PARTIAL,
        basePreset = draft.basePreset,
        themeConfig = draft.toThemeConfig(),
        createdAt = initialTheme?.createdAt ?: now,
        updatedAt = now
    )
}

@Composable
private fun PartialEditorHandle() {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier
            .size(width = 42.dp, height = 4.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(colors.borderMedium)
    )
}

@Composable
private fun PartialEditorTopBar(
    title: String,
    hasUnsavedChanges: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.width(84.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "Назад",
                    style = typography.bodyLarge,
                    color = colors.textPrimary,
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = typography.titleMedium,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (hasUnsavedChanges) {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(colors.selectedBackground)
                        .border(
                            width = 1.dp,
                            color = colors.selectedBorder,
                            shape = RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Не сохранено",
                        style = typography.bodySmall,
                        color = colors.textPrimary,
                        maxLines = 1
                    )
                }
            }
        }

        Box(
            modifier = Modifier.width(84.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = onSaveClick) {
                Text(
                    text = "Сохранить",
                    style = typography.bodyLarge,
                    color = colors.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun PartialEditorSectionTitle(text: String) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Text(
        text = text,
        style = typography.titleMedium,
        color = colors.textPrimary
    )
}

@Composable
private fun PartialThemePresetSelectorRow(
    selectedPreset: CalendarThemePreset,
    onPresetSelected: (CalendarThemePreset) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            CalendarThemePreset.Default,
            CalendarThemePreset.Ocean,
            CalendarThemePreset.Graphite
        ).forEach { preset ->
            val isSelected = preset == selectedPreset

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isSelected) colors.selectedBackground else colors.background
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) colors.selectedBorder else colors.borderLight,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onPresetSelected(preset) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = preset.name,
                    style = typography.bodyLarge,
                    color = colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun PartialEditorColorRow(
    field: ThemeColorField,
    resolvedColor: Color,
    isOverridden: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background, RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = if (isOverridden) colors.selectedBorder else colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(resolvedColor, CircleShape)
                .border(1.dp, colors.borderMedium, CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = field.title,
                style = typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = if (isOverridden) "Переопределено" else "Значение preset",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Text(
            text = resolvedColor.toHexArgbString(),
            style = typography.bodySmall,
            color = colors.textSecondary
        )
    }
}

private fun resolveBaseColorForPartialField(
    draft: ThemeEditorDraft,
    key: ThemeColorFieldKey
): Color {
    val resolved = resolveCalendarColors(
        draft.copy(
            colorOverrides = CalendarColorOverrides()
        ).toThemeConfig()
    )

    return when (key) {
        ThemeColorFieldKey.Primary -> resolved.primary
        ThemeColorFieldKey.PrimaryVariant -> resolved.primaryVariant
        ThemeColorFieldKey.Background -> resolved.background
        ThemeColorFieldKey.Surface -> resolved.surface
        ThemeColorFieldKey.TextPrimary -> resolved.textPrimary
        ThemeColorFieldKey.TextSecondary -> resolved.textSecondary
        ThemeColorFieldKey.TextTertiary -> resolved.textTertiary
        ThemeColorFieldKey.BorderLight -> resolved.borderLight
        ThemeColorFieldKey.BorderMedium -> resolved.borderMedium
        ThemeColorFieldKey.SelectedBackground -> resolved.selectedBackground
        ThemeColorFieldKey.SelectedBorder -> resolved.selectedBorder
        ThemeColorFieldKey.Income -> resolved.income
        ThemeColorFieldKey.IncomeVariant -> resolved.incomeVariant
        ThemeColorFieldKey.Expense -> resolved.expense
        ThemeColorFieldKey.ExpenseVariant -> resolved.expenseVariant
        ThemeColorFieldKey.AddSheetAccent -> resolved.addSheetAccent
        ThemeColorFieldKey.AddSheetAccentSecondary -> resolved.addSheetAccentSecondary
        ThemeColorFieldKey.AddSheetCategoryChip -> resolved.addSheetCategoryChip
        ThemeColorFieldKey.AddSheetRecurringAccent -> resolved.addSheetRecurringAccent
    }
}