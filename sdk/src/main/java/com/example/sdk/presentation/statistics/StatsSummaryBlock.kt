package com.example.sdk.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.GreenPrimary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatsSummaryBlock(
    incomeSum: Long,
    expenseSum: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(top = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
}

@Composable
private fun RowScope.StatsCard(
    sum: Long,
    isIncome: Boolean
) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardColors(
            containerColor = Color(0xFFF5F6F8),
            contentColor = Color(0xFFF5F6F8),
            disabledContainerColor = Color(0xFFF5F6F8),
            disabledContentColor = Color(0xFFF5F6F8)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val title = if (isIncome) R.string.incomes else R.string.expenses
            val textColor = if (isIncome) Color(0xFF10B981) else Color(0xFFF43F5E)
            Text(
                text = stringResource(title),
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
            Text(
                text = "${if (isIncome.not() && sum > 0) "-" else ""}${sum.formatSum()} ₽",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}


fun Long.formatSum(): String {
    return NumberFormat
        .getNumberInstance(Locale("ru", "RU"))
        .format(this)
}
