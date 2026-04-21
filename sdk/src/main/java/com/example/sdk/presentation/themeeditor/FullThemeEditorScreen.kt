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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.sdk.CalendarColorOverrides
import com.example.sdk.sdk.CalendarIconOverrides
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarTypographyOverrides
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.FinancialCalendarTheme
import com.example.sdk.ui.theme.resolveCalendarColors
import com.example.sdk.ui.theme.resolveCalendarIcons
import com.example.sdk.ui.theme.resolveCalendarTypography
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullThemeEditorScreen(
    initialTheme: CustomCalendarTheme? = null,
    defaultBasePreset: CalendarThemePreset = CalendarThemePreset.Default,
    editorThemeConfig: com.example.sdk.sdk.CalendarThemeConfig,
    onDismiss: () -> Unit,
    onRequestDismiss: (hasUnsavedChanges: Boolean) -> Unit,
    onApplyWithoutSave: (com.example.sdk.sdk.CalendarThemeConfig) -> Unit,
    onSave: (CustomCalendarTheme) -> Unit
) {
    var draft by remember(initialTheme, defaultBasePreset) {
        mutableStateOf(
            initialTheme?.toEditorDraft() ?: ThemeEditorDraft(
                id = null,
                name = "",
                mode = ThemeCustomizationMode.FULL,
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
            mode = ThemeCustomizationMode.FULL,
            basePreset = defaultBasePreset,
            colorOverrides = CalendarColorOverrides(),
            typographyOverrides = CalendarTypographyOverrides(),
            iconOverrides = CalendarIconOverrides()
        )
    }

    val hasUnsavedChanges = draft != initialDraft

    var activeColorField by remember { mutableStateOf<ThemeColorField?>(null) }
    var activeTypographyField by remember { mutableStateOf<ThemeTypographyField?>(null) }
    var activeIconField by remember { mutableStateOf<ThemeIconField?>(null) }

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
            FullEditorHandle()

            Spacer(modifier = Modifier.height(16.dp))

            FullEditorTopBar(
                title = if (initialTheme == null) {
                    "Новая полная тема"
                } else {
                    "Редактирование темы"
                },
                hasUnsavedChanges = hasUnsavedChanges,
                onBackClick = { onRequestDismiss(hasUnsavedChanges) },
                onSaveClick = { onSave(buildSavedTheme(initialTheme, draft)) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FullEditorSectionTitle(text = "Основное")

            Spacer(modifier = Modifier.height(10.dp))

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
                        text = "Например, Full Ocean",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            FullEditorSectionTitle(text = "Базовый preset")

            Spacer(modifier = Modifier.height(10.dp))

            FullThemePresetSelectorRow(
                selectedPreset = draft.basePreset,
                onPresetSelected = { preset ->
                    draft = draft.copy(basePreset = preset)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            FullEditorSectionTitle(text = "Превью")

            Spacer(modifier = Modifier.height(10.dp))

            ThemePreviewCard(draft = draft)

            Spacer(modifier = Modifier.height(18.dp))

            FullEditorSectionTitle(text = "Быстрые действия")

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        text = "Сделать светлой",
                        onClick = {
                            draft = draft.applyQuickLightTheme()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuickActionButton(
                        text = "Сделать тёмной",
                        onClick = {
                            draft = draft.applyQuickDarkTheme()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        text = "Сделать как Ocean",
                        onClick = {
                            draft = draft.copy(
                                basePreset = CalendarThemePreset.Ocean,
                                colorOverrides = CalendarColorOverrides()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    QuickActionButton(
                        text = "Сделать как Graphite",
                        onClick = {
                            draft = draft.copy(
                                basePreset = CalendarThemePreset.Graphite,
                                colorOverrides = CalendarColorOverrides()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            FullEditorSectionTitle(text = "Цвета")

            Spacer(modifier = Modifier.height(12.dp))

            ThemeColorFields.all.forEach { field ->
                FullEditorColorRow(
                    field = field,
                    resolvedColor = draft.colorOverrides.getColorValue(field.key)
                        ?: resolveBaseColorForField(draft, field.key),
                    isOverridden = draft.colorOverrides.getColorValue(field.key) != null,
                    onClick = { activeColorField = field }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            FullEditorSectionTitle(text = "Типографика")

            Spacer(modifier = Modifier.height(12.dp))

            ThemeTypographyFields.all.forEach { field ->
                val currentStyle = draft.typographyOverrides.getTextStyleValue(field.key)
                val fallbackStyle = resolveBaseTextStyleForField(draft, field.key)

                FullEditorTypographyRow(
                    field = field,
                    textStyle = currentStyle ?: fallbackStyle,
                    isOverridden = currentStyle != null,
                    onClick = { activeTypographyField = field }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            FullEditorSectionTitle(text = "Иконки")

            Spacer(modifier = Modifier.height(12.dp))

            ThemeIconFields.all.forEach { field ->
                val currentIcon = draft.iconOverrides.getIconValue(field.key)
                val fallbackIcon = resolveBaseIconForField(draft, field.key)

                FullEditorIconRow(
                    field = field,
                    iconRes = currentIcon ?: fallbackIcon,
                    isOverridden = currentIcon != null,
                    onClick = { activeIconField = field }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            style = typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }

                    TextButton(
                        onClick = {
                            draft = draft.copy(
                                typographyOverrides = CalendarTypographyOverrides()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Сбросить типографику",
                            style = typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            draft = draft.copy(
                                iconOverrides = CalendarIconOverrides()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Сбросить иконки",
                            style = typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }

                    TextButton(
                        onClick = {
                            onApplyWithoutSave(draft.toThemeConfig())
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Применить без сохранения",
                            style = typography.bodyMedium,
                            color = colors.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onSave(buildSavedTheme(initialTheme, draft))
                    },
                    modifier = Modifier.fillMaxWidth()
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

        if (activeTypographyField != null) {
            val field = activeTypographyField!!

            ModalBottomSheet(
                onDismissRequest = { activeTypographyField = null },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                containerColor = colors.surface
            ) {
                TypographyPickerBottomSheet(
                    field = field,
                    initialStyle = draft.typographyOverrides.getTextStyleValue(field.key),
                    onApply = { selectedStyle ->
                        draft = draft.copy(
                            typographyOverrides = draft.typographyOverrides.withTextStyleValue(
                                field.key,
                                selectedStyle
                            )
                        )
                        activeTypographyField = null
                    },
                    onDismiss = {
                        activeTypographyField = null
                    }
                )
            }
        }

        if (activeIconField != null) {
            val field = activeIconField!!

            ModalBottomSheet(
                onDismissRequest = { activeIconField = null },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                containerColor = colors.surface
            ) {
                IconPickerBottomSheet(
                    field = field,
                    initialIconRes = draft.iconOverrides.getIconValue(field.key),
                    onApply = { selectedIcon ->
                        draft = draft.copy(
                            iconOverrides = draft.iconOverrides.withIconValue(field.key, selectedIcon)
                        )
                        activeIconField = null
                    },
                    onDismiss = {
                        activeIconField = null
                    }
                )
            }
        }
    }
}

private fun buildSavedTheme(
    initialTheme: CustomCalendarTheme?,
    draft: ThemeEditorDraft
): CustomCalendarTheme {
    val now = System.currentTimeMillis()

    return CustomCalendarTheme(
        id = draft.id ?: UUID.randomUUID().toString(),
        name = resolveThemeNameForSave(
            rawName = draft.name,
            mode = ThemeCustomizationMode.FULL,
            basePreset = draft.basePreset
        ),
        mode = ThemeCustomizationMode.FULL,
        basePreset = draft.basePreset,
        themeConfig = draft.toThemeConfig(),
        createdAt = initialTheme?.createdAt ?: now,
        updatedAt = now
    )
}

private fun ThemeEditorDraft.applyQuickLightTheme(): ThemeEditorDraft {
    return copy(
        basePreset = CalendarThemePreset.Default,
        colorOverrides = CalendarColorOverrides(
            background = Color(0xFFF8FAFC),
            surface = Color(0xFFFFFFFF),
            textPrimary = Color(0xFF0F172A),
            textSecondary = Color(0xFF64748B),
            textTertiary = Color(0xFF94A3B8),
            borderLight = Color(0xFFE2E8F0),
            borderMedium = Color(0xFFCBD5E1),
            selectedBackground = Color(0x1400A86B),
            selectedBorder = Color(0xFF00A86B)
        )
    )
}

private fun ThemeEditorDraft.applyQuickDarkTheme(): ThemeEditorDraft {
    return copy(
        basePreset = CalendarThemePreset.Graphite,
        colorOverrides = CalendarColorOverrides(
            primary = Color(0xFF8B5CF6),
            primaryVariant = Color(0xFFA78BFA),
            background = Color(0xFF0F172A),
            surface = Color(0xFF111827),
            textPrimary = Color(0xFFF8FAFC),
            textSecondary = Color(0xFFCBD5E1),
            textTertiary = Color(0xFF94A3B8),
            borderLight = Color(0xFF1F2937),
            borderMedium = Color(0xFF334155),
            selectedBackground = Color(0x1F8B5CF6),
            selectedBorder = Color(0xFF8B5CF6),
            income = Color(0xFF22C55E),
            incomeVariant = Color(0xFF4ADE80),
            expense = Color(0xFFEF4444),
            expenseVariant = Color(0xFFF87171)
        )
    )
}

private fun resolveBaseColorForField(
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

private fun resolveBaseTextStyleForField(
    draft: ThemeEditorDraft,
    key: ThemeTypographyFieldKey
): TextStyle {
    val resolved = resolveCalendarTypography(
        draft.copy(
            typographyOverrides = CalendarTypographyOverrides()
        ).toThemeConfig()
    )

    return when (key) {
        ThemeTypographyFieldKey.TitleLarge -> resolved.titleLarge
        ThemeTypographyFieldKey.TitleMedium -> resolved.titleMedium
        ThemeTypographyFieldKey.BodyLarge -> resolved.bodyLarge
        ThemeTypographyFieldKey.BodyMedium -> resolved.bodyMedium
        ThemeTypographyFieldKey.BodySmall -> resolved.bodySmall
        ThemeTypographyFieldKey.LabelMedium -> resolved.labelMedium
        ThemeTypographyFieldKey.LabelSmall -> resolved.labelSmall
    }
}

private fun resolveBaseIconForField(
    draft: ThemeEditorDraft,
    key: ThemeIconFieldKey
): Int {
    val resolved = resolveCalendarIcons(
        draft.copy(
            iconOverrides = CalendarIconOverrides()
        ).toThemeConfig()
    )

    return when (key) {
        ThemeIconFieldKey.AddIcon -> resolved.add
        ThemeIconFieldKey.MonthTabIcon -> resolved.monthTab
        ThemeIconFieldKey.WeekTabIcon -> resolved.weekTab
        ThemeIconFieldKey.DayTabIcon -> resolved.dayTab
        ThemeIconFieldKey.CalendarBottomNavIcon -> resolved.calendarBottomNav
        ThemeIconFieldKey.ArrowLeftIcon -> resolved.arrowLeft
        ThemeIconFieldKey.ArrowRightIcon -> resolved.arrowRight
        ThemeIconFieldKey.RecurringIcon -> resolved.recurring
    }
}

@Composable
private fun FullEditorHandle() {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier
            .size(width = 42.dp, height = 4.dp)
            .background(colors.borderMedium, RoundedCornerShape(100.dp))
    )
}

@Composable
private fun FullEditorTopBar(
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
private fun FullEditorSectionTitle(text: String) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Text(
        text = text,
        style = typography.titleMedium,
        color = colors.textPrimary
    )
}

@Composable
private fun QuickActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = modifier
            .background(colors.background, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.bodyMedium,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun FullThemePresetSelectorRow(
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
                    .background(
                        if (isSelected) colors.selectedBackground else colors.background,
                        RoundedCornerShape(14.dp)
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
private fun FullEditorColorRow(
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

        Spacer(modifier = Modifier.width(10.dp))

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

@Composable
private fun FullEditorTypographyRow(
    field: ThemeTypographyField,
    textStyle: TextStyle,
    isOverridden: Boolean,
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
                color = if (isOverridden) colors.selectedBorder else colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = field.sampleText,
            style = textStyle,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun FullEditorIconRow(
    field: ThemeIconField,
    iconRes: Int,
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
                .size(34.dp)
                .background(colors.surface, RoundedCornerShape(12.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = iconRes,
                color = colors.textPrimary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

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
    }
}