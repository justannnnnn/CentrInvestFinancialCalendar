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
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import kotlin.math.abs

@Composable
fun TransactionsList() {
    val transactions = listOf(
        TransactionItem("Uber", "15.11.2025", -520, "🚕", 0xFF1E90FF),
        TransactionItem("Покупка кофе", "14.11.2025", -230, "☕", 0xFFFFA500),
        TransactionItem("Продукты", "14.11.2025", -1450, "🛒", 0xFFFFA500),
        TransactionItem("Такси", "14.11.2025", -380, "🚕", 0xFF1E90FF),
        TransactionItem("Кино", "12.11.2025", -600, "🎬", 0xFFFFB800),
        TransactionItem("Зарплата", "10.11.2025", 80_000, "💼", 0xFF00A86B),
        TransactionItem("Аптека", "10.11.2025", -850, "🏥", 0xFF8A2BE2),
        TransactionItem("Онлайн покупки", "08.11.2025", -2340, "💳", 0xFFF95E5A),
        TransactionItem("Супермаркет", "07.11.2025", -3200, "🛒", 0xFFFFA500)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        transactions.forEach { transaction ->
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
                    // Иконка категории
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(transaction.color).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = transaction.icon,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Название и дата
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = transaction.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray900
                        )
                        Text(
                            text = transaction.date,
                            fontSize = 14.sp,
                            color = Gray500
                        )
                    }
                }

                // Сумма
                Text(
                    text = if (transaction.amount < 0)
                        "- ${abs(transaction.amount)} ₽"
                    else
                        "+ ${transaction.amount} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (transaction.amount < 0) Gray900 else GreenPrimary
                )
            }

            if (transactions.last() != transaction) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray100)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}