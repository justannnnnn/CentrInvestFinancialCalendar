package com.example.sdk.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.example.sdk.ui.theme.CalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var uiState by remember { mutableStateOf(AddTransactionUiState()) }

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
                        isCategoryExpanded = false
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

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = true,
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