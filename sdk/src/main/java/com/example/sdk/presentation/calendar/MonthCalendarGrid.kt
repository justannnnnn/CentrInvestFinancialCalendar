package com.example.sdk.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.data.network.dto.TransactionType
import com.example.sdk.domain.model.DayData
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
    daysData: Map<Int, DayData>,
    onDaySelected: (Int) -> Unit
) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val firstDayOfMonth = Calendar.getInstance().apply {
        set(year, month, 1)
    }
    val firstDayOffset = (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7

    val allTransactions = daysData.values
        .flatMap { it.transactions }
        .filter { it.amount != 0L }
    val categoriesToSum = allTransactions
        .map { it.category to it.amount }
        .groupingBy { it.first }
        .fold(0L) { acc, pair -> acc + pair.second }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalendarGrid(
            daysInMonth = daysInMonth,
            firstDayOffset = firstDayOffset,
            selectedDay = selectedDay,
            daysData = daysData,
            onDaySelected = onDaySelected
        )

        StatsSummaryBlock(
            incomeSum = allTransactions.filter { it.type == TransactionType.INCOME }
                .sumOf { it.amount },
            expenseSum = allTransactions.filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }
        )
        Divider()
        DonutChartWithStats(categoriesToSum = categoriesToSum)
        StatsCategoryList(categoriesToSum = categoriesToSum)
        TransactionsList(transactions = allTransactions)
    }
}

@Composable
private fun CalendarGrid(
    daysInMonth: Int,
    firstDayOffset: Int,
    selectedDay: Int?,
    daysData: Map<Int, DayData>,
    onDaySelected: (Int) -> Unit
) {
    WeekDaysHeader()
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
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

            DayCell(
                day = day,
                isSelected = selectedDay == day,
                hasOperations = daysData[day]?.transactions?.any { it.isPlanned.not() } == true,
                hasRecurring = daysData[day]?.hasRecurring == true,
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
            .clickable { onClick() }
            .padding(top = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = day.toString(),
                color = if (isSelected) GreenPrimary else Gray900,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontSize = 16.sp
            )

            if (hasOperations || hasRecurring) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasOperations) Text("💳", fontSize = 10.sp)

                    if (hasRecurring) {
                        if (hasOperations) Spacer(modifier = Modifier.width(4.dp))

                        Text("🔁", fontSize = 10.sp)
                    }
                }
            }
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
private fun Divider() {
    Box(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}
