package com.example.sdk.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.offset

// Extension для поиска Activity из Context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView() {
    val context = LocalContext.current

    // Делаем статус-бар тёмным (чёрные иконки)
    remember {
        context.findActivity()?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = false // Чёрные иконки
            window.statusBarColor = android.graphics.Color.WHITE // Белый фон статус-бара
        }
    }

    var currentYearMonth by remember { mutableStateOf(YearMonth.of(2025, 11)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedViewMode by remember { mutableStateOf("month") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CalendarHeader(
                yearMonth = currentYearMonth,
                onPrevMonth = { currentYearMonth = currentYearMonth.minusMonths(1) },
                onNextMonth = { currentYearMonth = currentYearMonth.plusMonths(1) },
                onAddClick = { /* TODO: открыть добавление */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            ViewModeTabs(
                selectedMode = selectedViewMode,
                onModeSelected = { selectedViewMode = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedViewMode == "month") {
                MonthCalendarGrid(
                    yearMonth = currentYearMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date -> selectedDate = date },
                    dayHasOperations = { date -> date.dayOfMonth % 5 == 0 || date.dayOfMonth % 3 == 0 },
                    dayHasRecurring = { date -> date.dayOfMonth == 1 || date.dayOfMonth == 10 }
                )
            } else {
                Text(
                    text = "$selectedViewMode — в разработке",
                    modifier = Modifier.padding(16.dp),
                    color = Gray500
                )
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    yearMonth: YearMonth,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onAddClick: () -> Unit
) {
    val monthName = yearMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
        .replaceFirstChar { it.uppercase() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp) // Больше отступ снизу для кнопки
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Предыдущий месяц",
                    tint = Gray900,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onPrevMonth() }
                        .padding(6.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Gray100)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .clickable { /* PeriodSelector */ }
                ) {
                    Text(
                        text = "$monthName ${yearMonth.year}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = null,
                        tint = Gray900,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Следующий месяц",
                    tint = Gray900,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onNextMonth() }
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.width(56.dp)) // Зарезервировать место под кнопку
        }

        // Кнопка "+" вынесена поверх
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 20.dp) // Легко опускаем ниже верхнего края, но не на статус-бар
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(GreenPrimary)
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Добавить операцию",
                tint = White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun MonthCalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    dayHasOperations: (LocalDate) -> Boolean,
    dayHasRecurring: (LocalDate) -> Boolean
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOffset = (firstDayOfMonth.dayOfWeek.value % 7) // 0 = Понедельник

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Дни недели
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Gray500,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Пустые ячейки
            items(firstDayOffset) {
                Box(modifier = Modifier.size(48.dp))
            }

            // Дни
            items(daysInMonth) { index ->
                val date = firstDayOfMonth.plusDays(index.toLong())
                val isSelected = selectedDate == date
                val hasOps = dayHasOperations(date)
                val hasRec = dayHasRecurring(date)

                Box(
                    modifier = Modifier
                        .size(48.dp) // Фиксированный размер ячейки
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) GreenPrimary.copy(alpha = 0.12f) else White)
                        .border(
                            width = 1.5.dp,
                            color = if (isSelected) GreenPrimary else Gray100,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isSelected) GreenPrimary else Gray900,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 16.sp
                        )

                        if (hasOps || hasRec) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.Center) {
                                if (hasOps) Text("💳", fontSize = 14.sp)
                                if (hasRec) {
                                    if (hasOps) Spacer(modifier = Modifier.width(4.dp))
                                    Text("🔁", fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Отступ снизу для статистики потом
    }
}

@Composable
private fun ViewModeTabs(
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ViewModeTab(
            icon = "grid", // 2×2 точки
            label = "Месяц",
            isSelected = selectedMode == "month",
            onClick = { onModeSelected("month") }
        )
        ViewModeTab(
            icon = "week", // три полоски
            label = "Неделя",
            isSelected = selectedMode == "week",
            onClick = { onModeSelected("week") }
        )
        ViewModeTab(
            icon = "day", // одна полоска
            label = "День",
            isSelected = selectedMode == "day",
            onClick = { onModeSelected("day") }
        )
    }
}

@Composable
private fun ViewModeTab(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            when (icon) {
                "grid" -> MonthIcon(isSelected)
                "week" -> WeekIcon(isSelected)
                "day" -> DayIcon(isSelected)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
private fun MonthIcon(selected: Boolean) {
    val color = if (selected) White else Gray900.copy(alpha = 0.7f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
                Box(modifier = Modifier.size(7.dp).background(color, RoundedCornerShape(2.dp)))
            }
        }
    }
}

@Composable
private fun WeekIcon(selected: Boolean) {
    val color = if (selected) androidx.compose.ui.graphics.Color.White else Gray900.copy(alpha = 0.5f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(bg), contentAlignment = Alignment.Center) {
        Row(modifier = Modifier.width(24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            repeat(3) { Box(modifier = Modifier.width(3.dp).height(12.dp).background(color, RoundedCornerShape(1.dp))) }
        }
    }
}

@Composable
private fun DayIcon(selected: Boolean) {
    val color = if (selected) androidx.compose.ui.graphics.Color.White else Gray900.copy(alpha = 0.5f)
    val bg = if (selected) GreenPrimary else Gray100

    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(bg), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.width(10.dp).height(16.dp).background(color, RoundedCornerShape(2.dp)))
    }
}

@Composable
private fun MonthViewPlaceholder() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Сетка календаря на месяц\n(скоро будет здесь)",
            color = Gray900.copy(alpha = 0.6f),
            fontSize = 18.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}