package com.example.sdk.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.domain.model.Transaction
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun TransactionsList(transactions: List<Transaction>) {
    AllTransactionsHeader()

    Column(
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        transactions.sortedBy { it.date }.forEach { transaction ->
            if (transaction.category != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(transaction.category.color).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = transaction.category.icon,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(verticalArrangement = Arrangement.Center) {
                            val title = if (transaction.note.isNullOrEmpty()) {
                                transaction.category.title
                            } else {
                                transaction.note
                            }
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray900
                            )
                            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                            Text(
                                text = dateFormat.format(transaction.date.time),
                                fontSize = 14.sp,
                                color = Gray500
                            )
                        }
                    }

                    Text(
                        text = "${if (transaction.category.isIncome) "+" else "-"} " +
                                "${transaction.amount.formatSum()} ₽",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (transaction.category.isIncome) GreenPrimary else Gray900
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray100)
                )
            }
        }
    }
}