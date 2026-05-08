package com.example.sdk.presentation.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdk.data.network.dto.Recurrence
import com.example.sdk.data.network.dto.RecurrenceUnit
import com.example.sdk.domain.model.*
import com.example.sdk.ui.theme.CalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    categories: List<CalendarCategoryUi>,
    onDismiss: () -> Unit,
    onSave: (CalendarOperationUi) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var uiState by remember { 
        mutableStateOf(AddTransactionUiState(selectedCategory = categories.firstOrNull())) 
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 760.dp)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.width(44.dp),
                    colors = addSheetTextButtonColors()
                ) {
                    Text(
                        text = "✕",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Новая операция",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                }

                Spacer(modifier = Modifier.width(44.dp))
            }

            HorizontalDivider(color = colors.borderLight)

            Spacer(modifier = Modifier.height(20.dp))

            AddTransactionCategorySection(
                categories = categories,
                selectedCategory = uiState.selectedCategory,
                expanded = uiState.isCategoryExpanded,
                onToggleExpanded = {
                    uiState = uiState.copy(
                        isCategoryExpanded = !uiState.isCategoryExpanded
                    )
                },
                onCategorySelected = { category ->
                    uiState = uiState.copy(
                        selectedCategory = category,
                        isCategoryExpanded = false,
                        isIncome = category.isIncome
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(text = "Описание")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = {
                    uiState = uiState.copy(description = it)
                },
                placeholder = {
                    Text(
                        text = "Например: Покупка кофе",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = addSheetOutlinedTextFieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            AddTransactionAmountSection(
                amount = uiState.amount,
                onAmountChange = {
                    uiState = uiState.copy(amount = it)
                },
                isIncome = uiState.isIncome,
                onIncomeSelected = {
                    uiState = uiState.copy(isIncome = true)
                },
                onExpenseSelected = {
                    uiState = uiState.copy(isIncome = false)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AddTransactionRecurringSection(
                isRecurring = uiState.isRecurring,
                recurringValue = uiState.recurringValue,
                recurringUnitIndex = uiState.recurringUnitIndex,
                recurringUnits = AddTransactionConstants.recurringUnits,
                recurringTime = uiState.recurringTime,
                onRecurringToggle = {
                    uiState = uiState.copy(isRecurring = it)
                },
                onRecurringValueChange = {
                    uiState = uiState.copy(recurringValue = it)
                },
                onRecurringUnitClick = {
                    uiState = uiState.copy(
                        recurringUnitIndex = (uiState.recurringUnitIndex + 1) % AddTransactionConstants.recurringUnits.size
                    )
                },
                onRecurringTimeClick = {
                    val nextTime = when (uiState.recurringTime) {
                        "12:00" -> "09:00"
                        "09:00" -> "18:00"
                        else -> "12:00"
                    }
                    uiState = uiState.copy(recurringTime = nextTime)
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            val isSaveEnabled = uiState.amount.toLongOrNull() != null && uiState.selectedCategory != null

            Button(
                onClick = {
                    val operation = CalendarOperationUi(
                        id = 0, // Generated by backend
                        title = uiState.description.ifBlank { uiState.selectedCategory?.name ?: "" },
                        amount = uiState.amount.toDoubleOrNull() ?: 0.0,
                        dateTime = System.currentTimeMillis(),
                        categoryId = uiState.selectedCategory?.id ?: 0,
                        isCustom = true,
                        status = null, // Future/Planned if dateTime is now/future
                        recurrence = if (uiState.isRecurring) {
                            Recurrence(
                                every = uiState.recurringValue.toIntOrNull() ?: 1,
                                unit = when (uiState.recurringUnitIndex) {
                                    0 -> RecurrenceUnit.DAY
                                    1 -> RecurrenceUnit.WEEK
                                    else -> RecurrenceUnit.MONTH
                                },
                                time = uiState.recurringTime
                            )
                        } else null
                    )
                    onSave(operation)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isSaveEnabled,
                colors = addSheetPrimaryButtonColors()
            ) {
                Text(
                    text = "Сохранить",
                    style = typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = addSheetTextButtonColors()
            ) {
                Text(
                    text = "Отмена",
                    style = typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
