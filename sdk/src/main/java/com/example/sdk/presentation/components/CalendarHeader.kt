package com.example.sdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarHeader(
    calendar: Calendar,
    selectedViewMode: String,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onAddClick: () -> Unit,
    onPeriodClick: () -> Unit
) {
    val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("ru", "RU"))
        ?.replaceFirstChar { it.uppercase() } ?: "Месяц"

    val year = calendar.get(Calendar.YEAR)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = Gray900,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onPrevMonth() }
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Gray100)
                        .clickable { onPeriodClick() }
                        .padding(horizontal = 14.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when (selectedViewMode) {
                            "month" -> "$monthName $year"
                            "week" -> getWeekRange(calendar)
                            "day" -> getDayDate(calendar)
                            else -> "$monthName $year"
                        },
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("📅", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Вперёд",
                    tint = Gray900,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onNextMonth() }
                        .padding(4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(GreenPrimary)
                    .clickable { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Добавить операцию",
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

private fun getWeekRange(calendar: Calendar): String {
    val start = calendar.clone() as Calendar
    start.firstDayOfWeek = Calendar.MONDAY

    val currentDayOfWeek = start.get(Calendar.DAY_OF_WEEK)
    val offsetToMonday = when (currentDayOfWeek) {
        Calendar.SUNDAY -> -6
        else -> Calendar.MONDAY - currentDayOfWeek
    }
    start.add(Calendar.DAY_OF_MONTH, offsetToMonday)

    val end = start.clone() as Calendar
    end.add(Calendar.DAY_OF_MONTH, 6)

    val sameMonth = start.get(Calendar.MONTH) == end.get(Calendar.MONTH) &&
            start.get(Calendar.YEAR) == end.get(Calendar.YEAR)

    return if (sameMonth) {
        val monthYear = SimpleDateFormat("MMMM yyyy", Locale("ru")).format(end.time)
        "${start.get(Calendar.DAY_OF_MONTH)} - ${end.get(Calendar.DAY_OF_MONTH)} $monthYear"
    } else {
        val startFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val endFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        "${startFormat.format(start.time)} - ${endFormat.format(end.time)}"
    }
}

private fun getDayDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    return dateFormat.format(calendar.time)
}