package com.example.sdk.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
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
import com.example.sdk.ui.theme.White
import com.example.sdk.presentation.models.MockTransactions
import com.example.sdk.presentation.models.Transaction as MockTransaction
import com.example.sdk.domain.model.Transaction as DomainTransaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun BottomSheetContent(
    selectedDay: Int?,
    selectedMonth: Calendar = Calendar.getInstance(),
    transactions: List<DomainTransaction> = emptyList()
) {
    val day = selectedDay ?: 14

    // Получаем доменные транзакции
    val domainDayTransactions = if (transactions.isNotEmpty()) {
        transactions.filter { tx ->
            val txCal = tx.date.clone() as Calendar
            txCal.get(Calendar.YEAR) == selectedMonth.get(Calendar.YEAR) &&
                    txCal.get(Calendar.MONTH) == selectedMonth.get(Calendar.MONTH) &&
                    txCal.get(Calendar.DAY_OF_MONTH) == day
        }
    } else {
        emptyList()
    }

    // Получаем моковые транзакции
    val mockDayTransactions = if (transactions.isEmpty()) {
        MockTransactions.list.filter { tx ->
            tx.date.get(Calendar.DAY_OF_MONTH) == day
        }
    } else {
        emptyList()
    }

    // Используем доменные, если есть, иначе моковые
    val displayTransactions = if (domainDayTransactions.isNotEmpty()) {
        domainDayTransactions
    } else {
        mockDayTransactions
    }

    val totalAmount = if (displayTransactions.isNotEmpty()) {
        if (displayTransactions.first() is DomainTransaction) {
            (displayTransactions as List<DomainTransaction>).sumOf { it.amount }.toInt()
        } else {
            (displayTransactions as List<MockTransaction>).sumOf { it.amount }.toInt()
        }
    } else 0

    val mockDate = Calendar.getInstance().apply {
        set(2025, Calendar.NOVEMBER, day)
    }
    val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
    val formattedDate = dateFormat.format(mockDate.time)
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        // Верхняя панель с плюсиком
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenPrimary)
                    .clickable { /* Добавить операцию */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Дата
        Text(
            text = formattedDate,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Краткая статистика
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${displayTransactions.size} ${getOperationWord(displayTransactions.size)}",
                fontSize = 14.sp,
                color = Gray500
            )
            Text(
                text = if (totalAmount < 0) "-${abs(totalAmount)} ₽" else "+$totalAmount ₽",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (totalAmount < 0) Color(0xFFF95E5A) else GreenPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Список операций
        if (displayTransactions.isNotEmpty()) {
            if (displayTransactions.first() is DomainTransaction) {
                // Показываем доменные транзакции
                (displayTransactions as List<DomainTransaction>).forEach { tx ->
                    TransactionItem(
                        icon = getEmojiForCategory(tx.category),
                        name = tx.note ?: tx.category,
                        category = tx.category,
                        amount = tx.amount.toInt(),
                        color = when (tx.category) {
                            "Продукты" -> 0xFFFFA500
                            "Транспорт" -> 0xFF1E90FF
                            "Кафе" -> 0xFFFFA500
                            "Зарплата" -> 0xFF00A86B
                            else -> 0xFF808080
                        }
                    )
                }
            } else {
                // Показываем моковые транзакции
                (displayTransactions as List<MockTransaction>).forEach { tx ->
                    TransactionItem(
                        icon = tx.categoryEmoji,
                        name = tx.title,
                        category = tx.category,
                        amount = tx.amount.toInt(),
                        color = when (tx.category) {
                            "Продукты" -> 0xFFFFA500
                            "Транспорт" -> 0xFF1E90FF
                            "Кафе" -> 0xFFFFA500
                            "Зарплата" -> 0xFF00A86B
                            else -> 0xFF808080
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TransactionItem(
    icon: String,
    name: String,
    category: String,
    amount: Int,
    color: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Открыть детали */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
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
                    .background(Color(color).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Название и категория
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray900
                )
                Text(
                    text = category,
                    fontSize = 14.sp,
                    color = Gray500
                )
            }
        }

        // Сумма
        Text(
            text = if (amount < 0) "- ${abs(amount)} ₽" else "+ $amount ₽",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (amount < 0) Gray900 else GreenPrimary
        )
    }

    // Разделитель
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}

// для склонения слова "операция"
private fun getOperationWord(count: Int): String {
    return when {
        count == 1 -> "операция"
        count in 2..4 -> "операции"
        else -> "операций"
    }
}

private fun getEmojiForCategory(category: String): String {
    return when (category.lowercase()) {
        "продукты" -> "🛒"
        "транспорт" -> "🚕"
        "кафе" -> "☕"
        "зарплата" -> "💰"
        "жильё" -> "🏠"
        "подписки" -> "🎬"
        "развлечения" -> "🎥"
        else -> "💳"
    }
}