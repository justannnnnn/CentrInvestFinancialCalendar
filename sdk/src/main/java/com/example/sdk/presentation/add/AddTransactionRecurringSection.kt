package com.example.sdk.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddTransactionRecurringSection(
    isRecurring: Boolean,
    recurringValue: String,
    recurringUnitIndex: Int,
    recurringUnits: List<String>,
    recurringTime: String,
    onRecurringToggle: (Boolean) -> Unit,
    onRecurringValueChange: (String) -> Unit,
    onRecurringUnitClick: () -> Unit,
    onRecurringTimeClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val icons = CalendarTheme.icons

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(colors.addSheetRecurringAccent, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = icons.recurring,
                color = colors.addSheetAccent,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Регулярный платёж",
            style = typography.bodyLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isRecurring,
            onCheckedChange = onRecurringToggle,
            colors = addSheetSwitchColors()
        )
    }

    if (!isRecurring) return

    Spacer(modifier = Modifier.height(12.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            text = "Повторять каждые",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = recurringValue,
                onValueChange = onRecurringValueChange,
                modifier = Modifier.width(90.dp),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = addSheetOutlinedTextFieldColors()
            )

            SimpleDropdownField(
                value = recurringUnits[recurringUnitIndex],
                modifier = Modifier.weight(1f),
                onClick = onRecurringUnitClick
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Время списания",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        SimpleDropdownField(
            value = recurringTime,
            modifier = Modifier.fillMaxWidth(),
            trailingText = "◔",
            onClick = onRecurringTimeClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        val nextPayment = remember(recurringValue, recurringUnitIndex, recurringTime) {
            val calendar = Calendar.getInstance()
            val interval = recurringValue.toIntOrNull() ?: 30

            when (recurringUnitIndex) {
                0 -> calendar.add(Calendar.DAY_OF_MONTH, interval)
                1 -> calendar.add(Calendar.WEEK_OF_YEAR, interval)
                else -> calendar.add(Calendar.MONTH, interval)
            }

            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU"))
            "${formatter.format(calendar.time)} в $recurringTime"
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🗓",
                style = typography.bodySmall
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Следующий платёж: $nextPayment",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }
    }
}