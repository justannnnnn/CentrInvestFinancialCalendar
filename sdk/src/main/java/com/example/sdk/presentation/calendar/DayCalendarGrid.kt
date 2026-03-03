package com.example.sdk.presentation.calendar

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.domain.model.DayData
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.presentation.transactions.SwipeableTransaction
import com.example.sdk.presentation.transactions.SwipeableTransactionItem
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray300
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import com.example.sdk.utils.getDateFormat
import com.example.sdk.utils.getQuantityStringRu
import java.util.Calendar
import kotlin.math.abs

@Composable
fun DayCalendarGrid(
    calendar: Calendar,
    daysData: Map<Int, DayData>
) {
    val transactions = daysData[calendar.time.date]?.transactions.orEmpty()
    val income = transactions.filter { it.category?.isIncome == true }.sumOf { it.amount }
    val expense = transactions.filter { it.category?.isIncome == false }.sumOf { it.amount }

    val categoryMap = transactions
        .filter { it.category?.isIncome == false }
        .groupBy { it.category }
        .mapValues { (_, list) -> list.sumOf { it.amount } }
        .toList()
        .sortedByDescending { (_, amount) -> amount }
        .take(3)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = getDateFormat(calendar.time),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
                Text(
                    text = LocalContext.current.getQuantityStringRu(
                        R.plurals.number_of_operations,
                        transactions.size
                    ),
                    fontSize = 14.sp,
                    color = Gray500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF00A86B),
                                    Color(0xFF00C878)
                                )
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.incomes),
                            fontSize = 12.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "+${income.formatSum()} ₽",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF95E5A),
                                    Color(0xFFFF7875)
                                )
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.expenses),
                            fontSize = 12.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "-${expense.formatSum()} ₽",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }
            }
        }

        if (categoryMap.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.top_categories),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(categoryMap) { (category, amount) ->
                val percentage = (amount / abs(expense)) * 100
                category?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Открыть категорию */ },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Gray100
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.icon,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = it.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Gray900
                                    )
                                }
                                Text(
                                    text = "${amount.formatSum()} ₽",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray900
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Gray300)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(percentage / 100f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(GreenPrimary)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Text(
                text = stringResource(R.string.all_transactions),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gray900,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(transactions) { transaction ->
            transaction.category?.let {
                val title = if (transaction.note.isNullOrEmpty()) {
                    it.title
                } else {
                    transaction.note
                }
                SwipeableTransactionItem(
                    transaction = SwipeableTransaction(
                        id = transaction.hashCode(),
                        name = title,
                        amount = transaction.amount,
                        category = it
                    ),
                    onEdit = { /* Редактировать */ },
                    onDelete = { /* Удалить */ }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DayTransactionItem(
    transaction: DayTransaction
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showMenu = !showMenu }
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

                // Название и категория
                Column {
                    Text(
                        text = transaction.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray900
                    )
                    Text(
                        text = transaction.category,
                        fontSize = 14.sp,
                        color = Gray500
                    )
                }
            }

            // Сумма и меню
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (transaction.amount < 0)
                        "- ${abs(transaction.amount)} ₽"
                    else
                        "+ ${transaction.amount} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (transaction.amount < 0) Gray900 else GreenPrimary
                )

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Меню",
                    tint = Gray500,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { showMenu = !showMenu }
                        .padding(4.dp)
                )
            }
        }

        // Меню редактирования/удаления
        if (showMenu) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .shadow(4.dp)
                    .width(160.dp)
            ) {
                Column {
                    Text(
                        text = "✏️ Редактировать",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Редактировать */ }
                            .padding(12.dp),
                        fontSize = 14.sp,
                        color = Gray900
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Gray100)
                    )
                    Text(
                        text = "🗑️ Удалить",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* Удалить */ }
                            .padding(12.dp),
                        fontSize = 14.sp,
                        color = Color(0xFFF95E5A)
                    )
                }
            }
        }
    }

    // Разделитель
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}

private data class DayTransaction(
    val name: String,
    val amount: Int,
    val category: String,
    val icon: String,
    val color: Long
)

private fun getOperationWord(count: Int): String {
    return when {
        count == 1 -> "операция"
        count in 2..4 -> "операции"
        else -> "операций"
    }
}

private fun getCategoryInfo(category: String): CategoryInfo {
    return when (category.lowercase()) {
        "продукты" -> CategoryInfo("🛒", "Продукты")
        "транспорт" -> CategoryInfo("🚕", "Транспорт")
        "покупки" -> CategoryInfo("💳", "Покупки")
        else -> CategoryInfo("📦", category)
    }
}

private data class CategoryInfo(
    val icon: String,
    val name: String
)

// Вспомогательный модификатор для смещения
private fun Modifier.offset(y: Dp): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val yPx = y.roundToPx()
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, yPx)
        }
    }
)

@Composable
private fun SimpleTransactionItem(
    transaction: DayTransaction
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Открыть детали */ }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Иконка
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(transaction.color).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = transaction.icon,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Название
            Column {
                Text(
                    text = transaction.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray900
                )
                Text(
                    text = transaction.category,
                    fontSize = 12.sp,
                    color = Gray500
                )
            }
        }

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}