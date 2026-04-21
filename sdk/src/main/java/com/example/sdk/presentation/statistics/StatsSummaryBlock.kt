package com.example.sdk.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.sdk.R
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun StatsSummaryBlock(
    incomeSum: Long,
    expenseSum: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatsCard(
            sum = incomeSum,
            isIncome = true
        )
        StatsCard(
            sum = expenseSum,
            isIncome = false
        )
    }
}

@Composable
private fun RowScope.StatsCard(
    sum: Long,
    isIncome: Boolean
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.borderLight
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val title = if (isIncome) R.string.incomes else R.string.expenses
            val textColor = if (isIncome) colors.income else colors.expense

            Text(
                text = stringResource(title),
                style = typography.labelSmall,
                color = colors.textSecondary
            )
            Text(
                text = "${if (!isIncome && sum > 0) "-" else ""}${sum.formatSum()} ₽",
                style = typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = textColor
            )
        }
    }
}