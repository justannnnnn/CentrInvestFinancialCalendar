package com.example.sdk.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.sdk.R
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

private enum class PeriodOperationFilter(
    val title: String
) {
    ALL("Все"),
    INCOME("Доходы"),
    EXPENSE("Расходы")
}

private enum class PeriodOperationSort(
    val title: String
) {
    NEW_FIRST("Новые"),
    OLD_FIRST("Старые"),
    AMOUNT_DESC("Сумма ↓"),
    AMOUNT_ASC("Сумма ↑")
}

private data class CategoryAnalyticsItem(
    val category: CalendarCategoryUi,
    val amount: Double
)

@Composable
fun PeriodAnalyticsSheet(
    startDate: Calendar,
    endDate: Calendar,
    operations: List<CalendarOperationUi>,
    categories: List<CalendarCategoryUi>,
    onDismiss: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val scrollState = rememberScrollState()

    val normalizedStart = remember(startDate.timeInMillis) {
        clearStartOfDay(startDate.clone() as Calendar)
    }

    val normalizedEnd = remember(endDate.timeInMillis) {
        clearEndOfDay(endDate.clone() as Calendar)
    }

    val periodOperations = remember(
        normalizedStart.timeInMillis,
        normalizedEnd.timeInMillis,
        operations
    ) {
        operations.filter { operation ->
            operation.dateTime in normalizedStart.timeInMillis..normalizedEnd.timeInMillis
        }
    }

    var selectedFilter = remember {
        androidx.compose.runtime.mutableStateOf(PeriodOperationFilter.ALL)
    }

    var selectedSort = remember {
        androidx.compose.runtime.mutableStateOf(PeriodOperationSort.NEW_FIRST)
    }

    val filteredOperations = remember(
        periodOperations,
        selectedFilter.value,
        selectedSort.value
    ) {
        val filtered = when (selectedFilter.value) {
            PeriodOperationFilter.ALL -> periodOperations
            PeriodOperationFilter.INCOME -> periodOperations.filter { it.amount > 0.0 }
            PeriodOperationFilter.EXPENSE -> periodOperations.filter { it.amount < 0.0 }
        }

        when (selectedSort.value) {
            PeriodOperationSort.NEW_FIRST -> filtered.sortedByDescending { it.dateTime }
            PeriodOperationSort.OLD_FIRST -> filtered.sortedBy { it.dateTime }
            PeriodOperationSort.AMOUNT_DESC -> filtered.sortedByDescending { abs(it.amount) }
            PeriodOperationSort.AMOUNT_ASC -> filtered.sortedBy { abs(it.amount) }
        }
    }

    val income = periodOperations
        .filter { it.amount > 0.0 }
        .sumOf { it.amount }

    val expense = periodOperations
        .filter { it.amount < 0.0 }
        .sumOf { abs(it.amount) }

    val balance = income - expense

    val categorySummary = remember(filteredOperations, categories) {
        filteredOperations
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
            .map { entry ->
                CategoryAnalyticsItem(
                    category = entry.key,
                    amount = entry.value
                )
            }
            .sortedByDescending { it.amount }
            .take(6)
    }

    val groupedByDate = remember(filteredOperations) {
        filteredOperations.groupBy { operation ->
            dayKey(operation.dateTime)
        }
    }

    val dateFormat = remember {
        SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU"))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Аналитика за период",
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )

                        Text(
                            text = "${dateFormat.format(startDate.time)} — ${dateFormat.format(endDate.time)}",
                            style = typography.bodySmall,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            text = "×",
                            style = typography.titleLarge,
                            color = colors.textSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.borderLight)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AnalyticsMetricCard(
                            title = "Доходы",
                            value = "+${income.formatSum()} ₽",
                            color = colors.income,
                            modifier = Modifier.weight(1f)
                        )

                        AnalyticsMetricCard(
                            title = "Расходы",
                            value = "-${expense.formatSum()} ₽",
                            color = colors.expense,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AnalyticsBalanceCard(
                        balance = balance,
                        operationCount = periodOperations.size
                    )

                    PeriodFiltersBlock(
                        selectedFilter = selectedFilter.value,
                        selectedSort = selectedSort.value,
                        onFilterSelected = { filter ->
                            selectedFilter.value = filter
                        },
                        onSortSelected = { sort ->
                            selectedSort.value = sort
                        }
                    )

                    DonutAnalyticsBlock(
                        categorySummary = categorySummary,
                        totalAmount = categorySummary.sumOf { it.amount }
                    )

                    if (categorySummary.isNotEmpty()) {
                        Text(
                            text = "Топ категорий",
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )

                        categorySummary.forEach { item ->
                            CategoryAnalyticsRow(
                                category = item.category,
                                amount = item.amount
                            )
                        }
                    }

                    Text(
                        text = "Операции по датам",
                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary
                    )

                    if (filteredOperations.isEmpty()) {
                        EmptyAnalyticsBlock()
                    } else {
                        groupedByDate.forEach { (dateKey, dateOperations) ->
                            DateOperationsGroup(
                                dateKey = dateKey,
                                operations = dateOperations,
                                categories = categories
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun PeriodFiltersBlock(
    selectedFilter: PeriodOperationFilter,
    selectedSort: PeriodOperationSort,
    onFilterSelected: (PeriodOperationFilter) -> Unit,
    onSortSelected: (PeriodOperationSort) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.borderLight)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Фильтры",
            style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = colors.textPrimary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PeriodOperationFilter.entries.forEach { filter ->
                FilterChipSmall(
                    text = filter.title,
                    selected = filter == selectedFilter,
                    onClick = {
                        onFilterSelected(filter)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text(
            text = "Сортировка",
            style = typography.bodySmall,
            color = colors.textSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PeriodOperationSort.entries.take(2).forEach { sort ->
                FilterChipSmall(
                    text = sort.title,
                    selected = sort == selectedSort,
                    onClick = {
                        onSortSelected(sort)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PeriodOperationSort.entries.drop(2).forEach { sort ->
                FilterChipSmall(
                    text = sort.title,
                    selected = sort == selectedSort,
                    onClick = {
                        onSortSelected(sort)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FilterChipSmall(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(
                if (selected) {
                    colors.primary.copy(alpha = 0.12f)
                } else {
                    colors.surface
                }
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    colors.primary
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(13.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.bodySmall.copy(
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
            ),
            color = if (selected) {
                colors.primary
            } else {
                colors.textPrimary
            }
        )
    }
}

@Composable
private fun DonutAnalyticsBlock(
    categorySummary: List<CategoryAnalyticsItem>,
    totalAmount: Double
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.borderLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Структура периода",
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (categorySummary.isEmpty() || totalAmount <= 0.0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет данных для графика",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            } else {
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.size(150.dp)
                    ) {
                        val strokeWidth = 24.dp.toPx()
                        val chartSize = size.minDimension - strokeWidth
                        val topLeft = Offset(
                            x = strokeWidth / 2f,
                            y = strokeWidth / 2f
                        )

                        var startAngle = -90f

                        categorySummary.forEach { item ->
                            val sweep = ((item.amount / totalAmount) * 360f).toFloat()
                            val color = parseCategoryColor(item.category.color)

                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = topLeft,
                                size = Size(chartSize, chartSize),
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            )

                            startAngle += sweep
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Всего",
                            style = typography.bodySmall,
                            color = colors.textSecondary
                        )

                        Text(
                            text = "${totalAmount.formatSum()} ₽",
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categorySummary.take(4).forEach { item ->
                        DonutLegendRow(
                            item = item,
                            totalAmount = totalAmount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutLegendRow(
    item: CategoryAnalyticsItem,
    totalAmount: Double
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val percent = if (totalAmount > 0.0) {
        (item.amount / totalAmount) * 100.0
    } else {
        0.0
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(parseCategoryColor(item.category.color))
        )

        Text(
            text = item.category.name,
            style = typography.bodySmall,
            color = colors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )

        Text(
            text = "${percent.formatSum()}%",
            style = typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = colors.textSecondary
        )
    }
}

@Composable
private fun AnalyticsMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = modifier
            .height(82.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.12f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.20f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Text(
                text = title,
                style = typography.bodySmall,
                color = colors.textSecondary
            )

            Text(
                text = value,
                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

@Composable
private fun AnalyticsBalanceCard(
    balance: Double,
    operationCount: Int
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val isPositive = balance >= 0.0
    val balanceColor = if (isPositive) colors.income else colors.expense
    val balanceText = if (isPositive) {
        "+${balance.formatSum()} ₽"
    } else {
        "-${abs(balance).formatSum()} ₽"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.borderLight)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(balanceColor.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                tint = balanceColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = "Баланс периода",
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )

            Text(
                text = "$operationCount ${operationWord(operationCount)}",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Text(
            text = balanceText,
            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = balanceColor
        )
    }
}

@Composable
private fun CategoryAnalyticsRow(
    category: CalendarCategoryUi,
    amount: Double
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.borderLight)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryIcon(
            category = category
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = category.name,
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )

            Text(
                text = if (category.isIncome) "Доход" else "Расход",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Text(
            text = "${amount.formatSum()} ₽",
            style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = if (category.isIncome) colors.income else colors.expense
        )
    }
}

@Composable
private fun DateOperationsGroup(
    dateKey: String,
    operations: List<CalendarOperationUi>,
    categories: List<CalendarCategoryUi>
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val dayTotal = operations.sumOf { operation ->
        operation.amount
    }

    val isPositive = dayTotal >= 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.borderLight)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dateKey,
                    style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary
                )

                Text(
                    text = "${operations.size} ${operationWord(operations.size)}",
                    style = typography.bodySmall,
                    color = colors.textSecondary
                )
            }

            Text(
                text = if (isPositive) {
                    "+${dayTotal.formatSum()} ₽"
                } else {
                    "-${abs(dayTotal).formatSum()} ₽"
                },
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isPositive) colors.income else colors.expense
            )
        }

        operations.forEach { operation ->
            val category = categories.find { category ->
                category.id == operation.categoryId
            }

            PeriodOperationRow(
                operation = operation,
                category = category
            )
        }
    }
}

@Composable
private fun PeriodOperationRow(
    operation: CalendarOperationUi,
    category: CalendarCategoryUi?
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val isExpense = operation.amount < 0.0
    val operationColor = if (isExpense) colors.expense else colors.income

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (category != null) {
            CategoryIcon(category = category)
        } else {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.surface),
                contentAlignment = Alignment.Center
            ) {
                Text("❓")
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = operation.title,
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = colors.textPrimary
            )

            Text(
                text = category?.name ?: "Unknown",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Text(
            text = if (isExpense) {
                "-${abs(operation.amount).formatSum()} ₽"
            } else {
                "+${operation.amount.formatSum()} ₽"
            },
            style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = operationColor
        )
    }
}

@Composable
private fun CategoryIcon(
    category: CalendarCategoryUi
) {
    val parsedColor = remember(category.color) {
        parseCategoryColor(category.color)
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(parsedColor.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        if (category.iconUrl.startsWith("http")) {
            AsyncImage(
                model = category.iconUrl,
                contentDescription = category.name,
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = category.iconUrl.ifBlank { "❓" }
            )
        }
    }
}

@Composable
private fun EmptyAnalyticsBlock() {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.borderLight)
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "За выбранный период операций нет",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}

private fun operationWord(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "операция"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "операции"
        else -> "операций"
    }
}

private fun dayKey(timeMillis: Long): String {
    val formatter = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    return formatter.format(Calendar.getInstance().apply {
        timeInMillis = timeMillis
    }.time)
}

private fun parseCategoryColor(color: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        Color(0xFF9CA3AF)
    }
}

private fun clearStartOfDay(calendar: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

private fun clearEndOfDay(calendar: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
}