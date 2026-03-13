package com.example.sdk.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.presentation.models.Transaction
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transaction.categoryEmoji,
                fontSize = 32.sp,
                modifier = Modifier.size(40.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val sign = if (transaction.amount > 0) "+" else ""
            val color = if (transaction.amount > 0) GreenPrimary else MaterialTheme.colorScheme.error

            Text(
                text = "$sign${transaction.amount.toInt()} ₽",
                color = color,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )
        }
    }
}