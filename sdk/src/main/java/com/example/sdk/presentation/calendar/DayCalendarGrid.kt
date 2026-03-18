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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.presentation.transactions.SwipeableTransaction
import com.example.sdk.presentation.transactions.SwipeableTransactionItem
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray300
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

private enum class AmountSortOrder {
    Descending,
    Ascending
}

private enum class CategorySortOrder {
    Descending,
    Ascending
}

@Composable
fun DayCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    dayHasOperations: (Int) -> Boolean,
    dayHasRecurring: (Int) -> Boolean
) {
    val currentDay = selectedDay ?: calendar.get(Calendar.DAY_OF_MONTH)
    val mockDate = Calendar.getInstance().apply {
        set(2025, Calendar.NOVEMBER, currentDay)
    }

    val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
    val formattedDate = dateFormat.format(mockDate.time)
        .replaceFirstChar { it.uppercase() }

    var amountSortOrder by remember { mutableStateOf(AmountSortOrder.Descending) }
    var categorySortOrder by remember { mutableStateOf(CategorySortOrder.Descending) }

    // Моковые данные для демонстрации
    val transactions = listOf(
        DayTransaction("Покупка кофе", -230, "Покупки", "☕", 0xFFFFA500),
        DayTransaction("Продукты", -1450, "Продукты", "🛒", 0xFFFFA500),
        DayTransaction("Такси", -380, "Транспорт", "🚕", 0xFF1E90FF)
    )

    val sortedTransactions = remember(transactions, amountSortOrder) {
        when (amountSortOrder) {
            AmountSortOrder.Descending -> transactions.sortedByDescending { abs(it.amount) }
            AmountSortOrder.Ascending -> transactions.sortedBy { abs(it.amount) }
        }
    }

    val income = transactions.filter { it.amount > 0 }.sumOf { it.amount }
    val expense = transactions.filter { it.amount < 0 }.sumOf { it.amount }

    val categoryMap = remember(transactions, categorySortOrder) {
        val grouped = transactions
            .filter { it.amount < 0 }
            .groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { abs(it.amount) } }
            .toList()

        when (categorySortOrder) {
            CategorySortOrder.Descending -> grouped.sortedByDescending { (_, amount) -> amount }
            CategorySortOrder.Ascending -> grouped.sortedBy { (_, amount) -> amount }
        }.take(3)
    }

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
                    text = formattedDate,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
                Text(
                    text = "${transactions.size} ${getOperationWord(transactions.size)}",
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
                            text = "Доходы",
                            fontSize = 12.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "+${abs(income)} ₽",
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
                            text = "Расходы",
                            fontSize = 12.sp,
                            color = White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "-${abs(expense)} ₽",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Топ категорий",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )

                    SortCategoryButton(
                        sortOrder = categorySortOrder,
                        onClick = {
                            categorySortOrder = when (categorySortOrder) {
                                CategorySortOrder.Descending -> CategorySortOrder.Ascending
                                CategorySortOrder.Ascending -> CategorySortOrder.Descending
                            }
                        }
                    )
                }
            }

            items(categoryMap) { (category, amount) ->
                val percentage = if (abs(expense) == 0) {
                    0f
                } else {
                    amount.toFloat() / abs(expense).toFloat()
                }

                val categoryInfo = getCategoryInfo(category)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
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
                                    text = categoryInfo.icon,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = categoryInfo.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray900
                                )
                            }
                            Text(
                                text = "${abs(amount)} ₽",
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
                                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(GreenPrimary)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Все операции",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                SortAmountButton(
                    sortOrder = amountSortOrder,
                    onClick = {
                        amountSortOrder = when (amountSortOrder) {
                            AmountSortOrder.Descending -> AmountSortOrder.Ascending
                            AmountSortOrder.Ascending -> AmountSortOrder.Descending
                        }
                    }
                )
            }
        }

        items(sortedTransactions) { transaction ->
            SwipeableTransactionItem(
                transaction = SwipeableTransaction(
                    id = transaction.hashCode(),
                    name = transaction.name,
                    amount = transaction.amount,
                    category = transaction.category,
                    icon = transaction.icon,
                    color = transaction.color
                ),
                onEdit = { },
                onDelete = { }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SortAmountButton(
    sortOrder: AmountSortOrder,
    onClick: () -> Unit
) {
    val isDescending = sortOrder == AmountSortOrder.Descending

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDescending) GreenPrimary.copy(alpha = 0.10f) else Gray100)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (isDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
            contentDescription = "Сортировка по сумме",
            tint = if (isDescending) GreenPrimary else Gray500,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = if (isDescending) "↓ ₽" else "↑ ₽",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDescending) GreenPrimary else Gray500
        )
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
            }
        }

        if (showMenu) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = 40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .width(160.dp)
            ) {
                Column {
                    Text(
                        text = "✏️ Редактировать",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
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
                            .clickable { }
                            .padding(12.dp),
                        fontSize = 14.sp,
                        color = Color(0xFFF95E5A)
                    )
                }
            }
        }
    }

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
            .clickable { }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
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

@Composable
private fun SortCategoryButton(
    sortOrder: CategorySortOrder,
    onClick: () -> Unit
) {
    val isDescending = sortOrder == CategorySortOrder.Descending

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDescending) GreenPrimary.copy(alpha = 0.10f) else Gray100)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (isDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
            contentDescription = "Сортировка категорий по сумме",
            tint = if (isDescending) GreenPrimary else Gray500,
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = if (isDescending) "↓ ₽" else "↑ ₽",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDescending) GreenPrimary else Gray500
        )
    }
}