package com.example.sdk.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DayCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    operations: List<CalendarOperationUi>,
    categories: List<CalendarCategoryUi>,
    onDaySelected: (Int) -> Unit,
    onEditOperation: (CalendarOperationUi) -> Unit,
    onDeleteOperation: (CalendarOperationUi) -> Unit
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
        .filter { it.amount > 0.0 }
        .sumOf { it.amount }

    val expenseAbs = operations
        .filter { it.amount < 0.0 }
        .sumOf { abs(it.amount) }

    val filteredOperations = when (selectedFilter) {
        CategoryFilter.ALL -> operations
        CategoryFilter.INCOME -> operations.filter { it.amount > 0.0 }
        CategoryFilter.EXPENSE -> operations.filter { it.amount < 0.0 }
    }

    val totalAmount = filteredOperations.sumOf {
        abs(it.amount)
    }

    val categorySummary = filteredOperations
        .mapNotNull { operation ->
            val category = categories.find { category ->
                category.id == operation.categoryId
            }

            category?.let {
                it to abs(operation.amount)
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
                        .clickable {
                            selectedFilter =
                                if (selectedFilter == CategoryFilter.INCOME) {
                                    CategoryFilter.ALL
                                } else {
                                    CategoryFilter.INCOME
                                }
                        }
                        .padding(16.dp),
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
                        .clickable {
                            selectedFilter =
                                if (selectedFilter == CategoryFilter.EXPENSE) {
                                    CategoryFilter.ALL
                                } else {
                                    CategoryFilter.EXPENSE
                                }
                        }
                        .padding(16.dp),
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

        if (categorySummary.isNotEmpty() && totalAmount > 0.0) {
            item {
                Text(
                    text = "Топ категорий",
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(categorySummary) { (category, amount) ->
                val percentage = ((amount / totalAmount) * 100.0)
                    .toFloat()
                    .coerceIn(0f, 100f)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDaySelected(currentDay)
                        },
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
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            color = getCategoryColor(category.color).copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = category.iconUrl,
                                        contentDescription = category.name,
                                        modifier = Modifier.size(28.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }

                                Spacer(modifier = Modifier.size(8.dp))

                                Text(
                                    text = category.name,
                                    style = typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = colors.textPrimary
                                )
                            }

                            Text(
                                text = "${amount.formatSum()} ₽",
                                style = typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = if (category.isIncome) {
                                    colors.income
                                } else {
                                    colors.expense
                                }
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
                                            colors.income
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

        if (filteredOperations.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colors.borderLight
                    )
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
            items(
                items = filteredOperations,
                key = { operation ->
                    "${operation.id}_${operation.dateTime}_${operation.title}"
                }
            ) { operation ->
                val category = categories.find { category ->
                    category.id == operation.categoryId
                }

                SwipeableTransactionItem(
                    operation = operation,
                    category = category,
                    onEdit = {
                        onEditOperation(operation)
                    },
                    onDelete = {
                        onDeleteOperation(operation)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SwipeableTransactionItem(
    operation: CalendarOperationUi,
    category: CalendarCategoryUi?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    var offsetX by remember(operation.id, operation.dateTime, operation.title) {
        mutableFloatStateOf(0f)
    }

    val shape = RoundedCornerShape(16.dp)
    val itemHeight = 78.dp
    val maxSwipeOffset = 112f
    val swipeThreshold = 56f
    val isExpense = operation.amount < 0.0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .clip(shape)
            .background(colors.borderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFF4DE))
                    .clickable {
                        offsetX = 0f
                        onEdit()
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(start = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = Color(0xFFE69500),
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = "Изменить",
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFFE69500)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(colors.expense.copy(alpha = 0.14f))
                    .clickable {
                        offsetX = 0f
                        onDelete()
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Удалить",
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.expense
                    )

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = colors.expense,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = offsetX.roundToInt(),
                        y = 0
                    )
                }
                .fillMaxWidth()
                .height(itemHeight)
                .pointerInput(operation.id, operation.dateTime, operation.title) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()

                            offsetX = (offsetX + dragAmount)
                                .coerceIn(-maxSwipeOffset, maxSwipeOffset)
                        },
                        onDragEnd = {
                            offsetX = when {
                                offsetX > swipeThreshold -> maxSwipeOffset
                                offsetX < -swipeThreshold -> -maxSwipeOffset
                                else -> 0f
                            }
                        }
                    )
                }
                .clickable {
                    onEdit()
                },
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
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
                            .background(
                                color = category?.color
                                    ?.let { getCategoryColor(it).copy(alpha = 0.12f) }
                                    ?: colors.borderLight
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (category != null) {
                            AsyncImage(
                                model = category.iconUrl,
                                contentDescription = category.name,
                                modifier = Modifier.size(30.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text(
                                text = "❓",
                                style = typography.titleMedium
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text(
                            text = operation.title,
                            style = typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = colors.textPrimary
                        )

                        Text(
                            text = category?.name ?: "Unknown",
                            style = typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (isExpense) {
                            "-${abs(operation.amount).formatSum()} ₽"
                        } else {
                            "+${operation.amount.formatSum()} ₽"
                        },
                        style = typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isExpense) {
                            colors.expense
                        } else {
                            colors.income
                        }
                    )

                    Text(
                        text = "Свайп: изменить / удалить",
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }
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

private fun getCategoryColor(color: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        Color(0xFF9CA3AF)
    }
}

private enum class CategoryFilter {
    ALL,
    INCOME,
    EXPENSE
}