package com.example.sdk.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun SectionLabel(text: String) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Text(
        text = text,
        style = typography.bodyMedium,
        color = colors.textSecondary
    )
}

@Composable
fun AddTransactionAmountSection(
    amount: String,
    onAmountChange: (String) -> Unit,
    isIncome: Boolean,
    onIncomeSelected: () -> Unit,
    onExpenseSelected: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    SectionLabel(text = "Сумма")

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = addSheetOutlinedTextFieldColors()
        )

        Row(
            modifier = Modifier
                .height(56.dp)
                .background(colors.surface, RoundedCornerShape(18.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(18.dp))
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp)
                    .background(
                        if (!isIncome) colors.addSheetAccent else colors.surface,
                        RoundedCornerShape(
                            topStart = 18.dp,
                            bottomStart = 18.dp
                        )
                    )
                    .clickable { onExpenseSelected() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    style = typography.titleMedium,
                    color = if (!isIncome) colors.surface else colors.textSecondary
                )
            }

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp)
                    .background(
                        if (isIncome) colors.addSheetAccent else colors.surface,
                        RoundedCornerShape(
                            topEnd = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .clickable { onIncomeSelected() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = typography.titleMedium,
                    color = if (isIncome) colors.surface else colors.textSecondary
                )
            }
        }
    }
}

@Composable
fun SimpleDropdownField(
    value: String,
    modifier: Modifier = Modifier,
    trailingText: String = "⌄",
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = modifier
            .height(56.dp)
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(1.dp, colors.borderLight, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = value,
            style = typography.bodyLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = trailingText,
            style = typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}

@Composable
fun addSheetOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CalendarTheme.colors.addSheetAccent,
    unfocusedBorderColor = CalendarTheme.colors.borderMedium,
    disabledBorderColor = CalendarTheme.colors.borderLight,
    errorBorderColor = CalendarTheme.colors.expense,

    focusedTextColor = CalendarTheme.colors.textPrimary,
    unfocusedTextColor = CalendarTheme.colors.textPrimary,
    disabledTextColor = CalendarTheme.colors.textSecondary,
    errorTextColor = CalendarTheme.colors.textPrimary,

    focusedPlaceholderColor = CalendarTheme.colors.textSecondary,
    unfocusedPlaceholderColor = CalendarTheme.colors.textSecondary,
    disabledPlaceholderColor = CalendarTheme.colors.textTertiary,
    errorPlaceholderColor = CalendarTheme.colors.textSecondary,

    focusedContainerColor = CalendarTheme.colors.surface,
    unfocusedContainerColor = CalendarTheme.colors.surface,
    disabledContainerColor = CalendarTheme.colors.surface,
    errorContainerColor = CalendarTheme.colors.surface,

    cursorColor = CalendarTheme.colors.addSheetAccent
)

@Composable
fun addSheetSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = CalendarTheme.colors.surface,
    checkedTrackColor = CalendarTheme.colors.addSheetAccent,
    checkedBorderColor = CalendarTheme.colors.addSheetAccent,

    uncheckedThumbColor = CalendarTheme.colors.surface,
    uncheckedTrackColor = CalendarTheme.colors.borderMedium,
    uncheckedBorderColor = CalendarTheme.colors.borderMedium
)

@Composable
fun addSheetPrimaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = CalendarTheme.colors.addSheetAccent,
    contentColor = CalendarTheme.colors.surface,
    disabledContainerColor = CalendarTheme.colors.borderMedium,
    disabledContentColor = CalendarTheme.colors.textSecondary
)

@Composable
fun addSheetTextButtonColors() = ButtonDefaults.textButtonColors(
    contentColor = CalendarTheme.colors.textSecondary,
    disabledContentColor = CalendarTheme.colors.textTertiary
)