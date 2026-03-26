package com.example.sdk.presentation.calendar

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.util.Calendar
import com.example.sdk.presentation.statistics.StatsSummaryBlock
import com.example.sdk.presentation.statistics.StatsCategoryList
import com.example.sdk.presentation.statistics.DonutChartWithStats
import com.example.sdk.presentation.transactions.AllTransactionsHeader
import com.example.sdk.presentation.transactions.TransactionsList

@Composable
fun MonthCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    dayHasOperations: (Int) -> Boolean,
    dayHasRecurring: (Int) -> Boolean
) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val firstDayOfMonth = Calendar.getInstance().apply {
        set(year, month, 1)
    }
    val firstDayOffset = (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Дни недели
        item {
            WeekDaysHeader()
        }

        // Календарная сетка
        item {
            CalendarGrid(
                daysInMonth = daysInMonth,
                firstDayOffset = firstDayOffset,
                selectedDay = selectedDay,
                onDaySelected = onDaySelected,
                dayHasOperations = dayHasOperations,
                dayHasRecurring = dayHasRecurring
            )
        }

        // Блок статистики доходы/расходы
        item {
            StatsSummaryBlock()
        }

        // Разделитель
        item {
            Divider()
        }

        // Donut Chart с реальными данными
        item {
            DonutChartWithStats()
        }

        // Список категорий с иконками
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StatsCategoryList()
        }

        // Заголовок "Все операции"
        item {
            Spacer(modifier = Modifier.height(24.dp))
            AllTransactionsHeader()
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Список операций
        item {
            TransactionsList()
        }

        // Дополнительный отступ снизу
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun WeekDaysHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Gray500,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    daysInMonth: Int,
    firstDayOffset: Int,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    dayHasOperations: (Int) -> Boolean,
    dayHasRecurring: (Int) -> Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(
                (((daysInMonth + firstDayOffset + 6) / 7) * 56).dp
            ),
        userScrollEnabled = false
    ) {
        items(firstDayOffset) {
            Box(modifier = Modifier.size(48.dp))
        }

        items(daysInMonth) { index ->
            val day = index + 1
            val isSelected = selectedDay == day
            val hasOps = dayHasOperations(day)
            val hasRec = dayHasRecurring(day)

            DayCell(
                day = day,
                isSelected = isSelected,
                hasOperations = hasOps,
                hasRecurring = hasRec,
                onClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    hasOperations: Boolean,
    hasRecurring: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) GreenPrimary.copy(alpha = 0.12f) else White)
            .border(
                width = 1.dp,
                color = if (isSelected) GreenPrimary else Gray100,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day.toString(),
                color = if (isSelected) GreenPrimary else Gray900,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontSize = 16.sp
            )

            Box(
                modifier = Modifier.height(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasOperations) Text("💳", fontSize = 12.sp)
                    if (hasRecurring) {
                        if (hasOperations) Spacer(modifier = Modifier.width(4.dp))
                        Text("🔁", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun Divider() {
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
    Spacer(modifier = Modifier.height(16.dp))
}
