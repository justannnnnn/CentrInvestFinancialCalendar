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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sdk.R
import com.example.sdk.domain.model.DayData
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import java.util.Calendar

@Composable
fun WeekCalendarGrid(
    calendar: Calendar,
    selectedDay: Int?,
    daysData: Map<Int, DayData>,
    onDaySelected: (Int) -> Unit,
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val weekDays = getWeekDays(calendar, daysData)
    val weekTotal = weekDays.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.week_summary),
                    style = typography.bodyLarge,
                    color = colors.textSecondary
                )
                Text(
                    text = "${weekTotal.formatSum()} ₽",
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (weekTotal < 0) colors.expense else colors.primary
                )
            }
        }

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
                        hasOperations = daysData[dayItem.day]?.transactions
                            ?.any { it.isPlanned.not() } == true,
                        hasRecurring = daysData[dayItem.day]?.hasRecurring == true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

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
                        hasOperations = daysData[dayItem.day]?.transactions
                            ?.any { it.isPlanned.not() } == true,
                        hasRecurring = daysData[dayItem.day]?.hasRecurring == true,
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
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .background(if (isSelected) colors.selectedBackground else colors.surface)
            .border(
                width = 2.dp,
                color = if (isSelected) colors.selectedBorder else colors.borderLight,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = dayItem.dayOfWeek,
            style = typography.labelSmall,
            color = colors.textSecondary
        )

        Text(
            text = dayItem.day.toString(),
            style = typography.titleLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) colors.primary else colors.textPrimary
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.height(20.dp)
        ) {
            if (hasOperations) {
                Text(
                    text = "💳",
                    style = typography.labelSmall
                )
            }
            if (hasRecurring) {
                if (hasOperations) {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = "🔁",
                    style = typography.labelSmall
                )
            }
        }

        Text(
            text = dayItem.amount.formatSum(),
            style = typography.labelSmall,
            color = if (dayItem.amount < 0) colors.expense else colors.primary
        )
    }
}

private data class WeekDayItem(
    val day: Int,
    val dayOfWeek: String,
    val amount: Long
)

private fun getWeekDays(
    calendar: Calendar,
    daysData: Map<Int, DayData>
): List<WeekDayItem> {
    val weekDays = mutableListOf<WeekDayItem>()
    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    val calendarCopy = calendar.clone() as Calendar
    calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

    repeat(7) { index ->
        val day = calendarCopy.get(Calendar.DAY_OF_MONTH)
        weekDays.add(
            WeekDayItem(
                day = day,
                dayOfWeek = daysOfWeek[index],
                amount = daysData[day]?.transactions
                    ?.sumOf { if (it.category?.isIncome == true) it.amount else -it.amount } ?: 0
            )
        )
        calendarCopy.add(Calendar.DAY_OF_MONTH, 1)
    }

    return weekDays
}