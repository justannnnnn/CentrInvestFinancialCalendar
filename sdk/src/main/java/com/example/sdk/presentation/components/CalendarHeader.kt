package com.example.sdk.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray700
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

@Composable
fun CalendarHeader(
    calendar: Calendar,
    selectedViewMode: ViewModeTab,  // ← теперь это объект, а не строка!
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onAddClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ArrowButton(
                    isLeft = true,
                    onClick = onPrevMonth
                )

                PeriodSelector(
                    title = when (selectedViewMode) {
                        ViewModeTab.Day -> getDayDate(calendar)
                        ViewModeTab.Week -> getWeekRange(calendar)
                        ViewModeTab.Month ->
                            "${getMonthNameNominative(calendar)} ${calendar.get(Calendar.YEAR)}"
                    },
                    onPeriodSelectorClick = {}
                )

                ArrowButton(
                    isLeft = false,
                    onClick = onNextMonth
                )
            }
            PlusButton(onClick = onAddClick)
        }
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GreenPrimary)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        IconWrapper(
            modifier = Modifier.size(28.dp),
            iconRes = R.drawable.plus,
            color = White
        )
    }
}

@Composable
private fun PeriodSelector(
    title: String,
    onPeriodSelectorClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onPeriodSelectorClick)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = title,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.W400,
            color = Gray900
        )
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                modifier = Modifier.fillMaxSize(),
                iconRes = R.drawable.calendar,
                color = Gray500
            )
        }
    }
}

@Composable
private fun ArrowButton(
    isLeft: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val icon = if (isLeft) R.drawable.chevron_left else R.drawable.chevron_right
        IconWrapper(
            iconRes = icon,
            color = Gray700,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun IconWrapper(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    color: Color
) {
    Icon(
        modifier = modifier,
        painter = painterResource(iconRes),
        tint = color,
        contentDescription = null
    )
}

fun getMonthNameNominative(calendar: Calendar, locale: Locale = Locale("ru", "RU")): String {
    val monthIndex = calendar.get(Calendar.MONTH)
    val ruMonths = arrayOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )

    val name = if (locale.language == "ru") {
        ruMonths[monthIndex]
    } else {
        DateFormatSymbols(locale).months[monthIndex]
    }

    return name.replaceFirstChar { it.uppercase(locale) }
}

private fun getWeekRange(calendar: Calendar): String {
    val dayFormat = SimpleDateFormat("d", Locale("ru"))
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("ru"))
    val calendarCopy = calendar.clone() as Calendar

    // Начало недели (понедельник)
    calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startDay = dayFormat.format(calendarCopy.time)

    // Конец недели (воскресенье)
    calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    val endDay = dayFormat.format(calendarCopy.time)
    val monthYear = monthFormat.format(calendarCopy.time)

    return "$startDay - $endDay $monthYear"
}

private fun getDayDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    return dateFormat.format(calendar.time)
}