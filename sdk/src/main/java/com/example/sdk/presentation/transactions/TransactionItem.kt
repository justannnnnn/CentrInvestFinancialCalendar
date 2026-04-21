package com.example.sdk.presentation.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sdk.domain.model.Transaction
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val category = transaction.category
    val title = if (transaction.note.isNullOrBlank()) category.title else transaction.note
    val sign = if (transaction.amount > 0) "+" else ""
    val amountColor = if (transaction.amount > 0) colors.primary else colors.expense

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.icon,
                style = typography.titleLarge,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = colors.textPrimary
                )
                Text(
                    text = category.title,
                    style = typography.bodySmall,
                    color = colors.textSecondary
                )
            }

            Text(
                text = "$sign${transaction.amount} ₽",
                style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = amountColor
            )
        }
    }
}