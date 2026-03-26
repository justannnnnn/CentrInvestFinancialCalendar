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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.sdk.ui.theme.Gray600
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.util.Calendar
import kotlin.math.abs

@Composable
fun WeekCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    dayHasOperations: (Int) -> Boolean,
    dayHasRecurring: (Int) -> Boolean
) {
    val weekDays = getWeekDays(calendar)
    val weekTotal = weekDays.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Итого за неделю
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Итого за неделю:",
                    fontSize = 16.sp,
                    color = Gray500
                )
                Text(
                    text = if (weekTotal < 0) "- ${abs(weekTotal)} ₽" else "+ $weekTotal ₽",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (weekTotal < 0) Color(0xFFF95E5A) else GreenPrimary
                )
            }
        }

        // Первая строка - 4 дня (Пн, Вт, Ср, Чт)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weekDays.take(4).forEach { dayItem ->
                    WeekDayCard(
                        dayItem = dayItem,
                        isSelected = selectedDay == dayItem.day,
                        onClick = { onDaySelected(dayItem.day) },
                        hasOperations = dayHasOperations(dayItem.day),
                        hasRecurring = dayHasRecurring(dayItem.day),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Вторая строка - 3 дня (Пт, Сб, Вс)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weekDays.takeLast(3).forEach { dayItem ->
                    WeekDayCard(
                        dayItem = dayItem,
                        isSelected = selectedDay == dayItem.day,
                        onClick = { onDaySelected(dayItem.day) },
                        hasOperations = dayHasOperations(dayItem.day),
                        hasRecurring = dayHasRecurring(dayItem.day),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekDayCard(
    dayItem: WeekDayItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    hasOperations: Boolean,
    hasRecurring: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) GreenPrimary.copy(alpha = 0.05f)
                else White
            )
            .border(
                width = 2.dp,
                color = if (isSelected) GreenPrimary else Gray100,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // День недели
        Text(
            text = dayItem.dayOfWeek,
            fontSize = 12.sp,
            color = Gray500
        )

        // Число
        Text(
            text = dayItem.day.toString(),
            fontSize = 24.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) GreenPrimary else Gray900
        )

        // Иконки операций
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(20.dp)
        ) {
            if (hasOperations) Text("💳", fontSize = 12.sp)
            if (hasRecurring) {
                if (hasOperations) Spacer(modifier = Modifier.width(4.dp))
                Text("🔁", fontSize = 12.sp)
            }
        }

        // Сумма за день
        Text(
            text = if (dayItem.amount < 0)
                "- ${abs(dayItem.amount)}"
            else
                "+ ${dayItem.amount}",
            fontSize = 12.sp,
            color = if (dayItem.amount < 0) Gray600 else GreenPrimary
        )
    }
}

private data class WeekDayItem(
    val day: Int,
    val dayOfWeek: String,
    val amount: Int
)

private fun getWeekDays(calendar: Calendar): List<WeekDayItem> {
    val weekDays = mutableListOf<WeekDayItem>()
    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    val startOfWeek = calendar.clone() as Calendar
    startOfWeek.firstDayOfWeek = Calendar.MONDAY

    val currentDayOfWeek = startOfWeek.get(Calendar.DAY_OF_WEEK)
    val offsetToMonday = when (currentDayOfWeek) {
        Calendar.SUNDAY -> -6
        else -> Calendar.MONDAY - currentDayOfWeek
    }
    startOfWeek.add(Calendar.DAY_OF_MONTH, offsetToMonday)

    repeat(7) { index ->
        val day = startOfWeek.get(Calendar.DAY_OF_MONTH)
        weekDays.add(
            WeekDayItem(
                day = day,
                dayOfWeek = daysOfWeek[index],
                amount = getAmountForDay(day)
            )
        )
        startOfWeek.add(Calendar.DAY_OF_MONTH, 1)
    }

    return weekDays
}
private fun getAmountForDay(day: Int): Int {
    return when (day) {
        11 -> -450
        12 -> 2000
        13 -> -1200
        14 -> -890
        15 -> -340
        16 -> -2100
        17 -> 500
        else -> -(day * 100)
    }
}