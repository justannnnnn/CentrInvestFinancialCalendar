package com.example.sdk.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@Composable
fun DayCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    operations: List<CalendarOperationUi>,
    categories: List<CalendarCategoryUi>,
    onDaySelected: (Int) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val currentDay = selectedDay ?: calendar.get(Calendar.DAY_OF_MONTH)

    val selectedDateCalendar = (calendar.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, currentDay)
    }
    var selectedFilter by remember {
        mutableStateOf(CategoryFilter.ALL)
    }

    val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
    val formattedDate = dateFormat.format(selectedDateCalendar.time)
        .replaceFirstChar { it.uppercase() }

    val income = operations
        .filter { it.amount > 0 }
        .sumOf { it.amount }

    val expenseAbs = operations
        .filter { it.amount < 0 }
        .sumOf { abs(it.amount) }

    val filteredOperations = when (selectedFilter) {

        CategoryFilter.ALL -> operations

        CategoryFilter.INCOME -> {
            operations.filter { it.amount > 0 }
        }

        CategoryFilter.EXPENSE -> {
            operations.filter { it.amount < 0 }
        }
    }

    val totalAmount = filteredOperations.sumOf {
        abs(it.amount)
    }

    val categorySummary = filteredOperations
        .mapNotNull { op ->
            categories.find { it.id == op.category?.id }
                ?.let { category ->
                    category to abs(op.amount)
                }
        }
        .groupBy { it.first }
        .mapValues { entry ->
            entry.value.sumOf { it.second }
        }
        .toList()
        .sortedByDescending { it.second }
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
                    text = formattedDate,
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary
                )

                Text(
                    text = "${operations.size} ${getOperationWord(operations.size)}",
                    style = typography.bodyMedium,
                    color = colors.textSecondary,
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
                                    colors.income,
                                    colors.incomeVariant
                                )
                            )
                        )
                        .border(
                            width = if (selectedFilter == CategoryFilter.INCOME) 6.dp else 0.dp,
                            color = colors.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .clickable {
                            selectedFilter =
                                if (selectedFilter == CategoryFilter.INCOME) {
                                    CategoryFilter.ALL
                                } else {
                                    CategoryFilter.INCOME
                                }
                        },
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "Доходы",
                            style = typography.labelSmall,
                            color = colors.surface.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "+${income.formatSum()} ₽",
                            style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = colors.surface
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
                                    colors.expense,
                                    colors.expenseVariant
                                )
                            )
                        )
                        .border(
                            width = if (selectedFilter == CategoryFilter.EXPENSE) 6.dp else 0.dp,
                            color = colors.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .clickable {
                            selectedFilter =
                                if (selectedFilter == CategoryFilter.EXPENSE) {
                                    CategoryFilter.ALL
                                } else {
                                    CategoryFilter.EXPENSE
                                }
                        },
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "Расходы",
                            style = typography.labelSmall,
                            color = colors.surface.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "-${expenseAbs.formatSum()} ₽",
                            style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = colors.surface
                        )
                    }
                }
            }
        }

        if (categorySummary.isNotEmpty() && expenseAbs > 0.0) {
            item {
                Text(
                    text = "Топ категорий",
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(categorySummary) { (category, amount) ->
                val percentage = ((amount / expenseAbs) * 100.0).toFloat().coerceIn(0f, 100f)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDaySelected(currentDay) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colors.borderLight
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
                                AsyncImage(
                                    model = category.iconUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp).padding(end = 8.dp)
                                )
                                Text(
                                    text = category.name,
                                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                    color = colors.textPrimary
                                )
                            }

                            Text(
                                text = "${amount.formatSum()} ₽",
                                style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = colors.textPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(colors.borderMedium)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(percentage / 100f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (category.isIncome) {
                                            colors.primary
                                        } else {
                                            colors.expense
                                        }
                                    )
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Все операции",
                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (operations.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.borderLight)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "На этот день операций нет",
                            style = typography.bodyMedium,
                            color = colors.textSecondary
                        )
                    }
                }
            }
        } else {
            items(filteredOperations) { operation ->
                val category = categories.find { it.id == operation.category?.id }
                SimpleTransactionItem(operation = operation, category = category)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SimpleTransactionItem(
    operation: CalendarOperationUi,
    category: CalendarCategoryUi?
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val isExpense = operation.amount < 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.borderLight),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = category?.iconUrl,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = operation.title,
                        style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = colors.textPrimary
                    )

                    Text(
                        text = category?.name ?: "Unknown",
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }

            Text(
                text = "${operation.amount.formatSum()} ₽",
                style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (isExpense) colors.expense else colors.income
            )
        }
    }
}

private fun getOperationWord(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "операция"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "операции"
        else -> "операций"
    }
}

enum class CategoryFilter {
    ALL,
    INCOME,
    EXPENSE
}
