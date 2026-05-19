package com.example.sdk.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.sdk.R
import com.example.sdk.ui.theme.CalendarTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private enum class EditingDateField {
    EXACT_DAY,
    START,
    END
}

@Composable
fun PeriodPickerOverlay(
    initialCalendar: Calendar,
    onDismiss: () -> Unit,
    onDaySelected: (Calendar) -> Unit,
    onApplyPeriod: (Calendar, Calendar) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val initialStart = remember(initialCalendar.timeInMillis) {
        clearTime(initialCalendar.clone() as Calendar)
    }

    val initialEnd = remember(initialCalendar.timeInMillis) {
        (clearTime(initialCalendar.clone() as Calendar)).apply {
            add(Calendar.DAY_OF_MONTH, 30)
        }
    }

    var startDate by remember(initialCalendar.timeInMillis) {
        mutableStateOf(initialStart)
    }

    var endDate by remember(initialCalendar.timeInMillis) {
        mutableStateOf(initialEnd)
    }

    var exactDay by remember(initialCalendar.timeInMillis) {
        mutableStateOf(initialStart)
    }

    var editingField by remember {
        mutableStateOf<EditingDateField?>(null)
    }

    var visibleMonth by remember(initialCalendar.timeInMillis) {
        mutableStateOf(
            (initialCalendar.clone() as Calendar).apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }
        )
    }

    val dateFormatter = remember {
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
                .padding(horizontal = 22.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Выбор периода",
                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary
                    )

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
                        .padding(16.dp)
                ) {
                    JumpToDayCard(
                        dateText = dateFormatter.format(exactDay.time),
                        isActive = editingField == EditingDateField.EXACT_DAY,
                        onClick = {
                            editingField = EditingDateField.EXACT_DAY
                            visibleMonth = (exactDay.clone() as Calendar).apply {
                                set(Calendar.DAY_OF_MONTH, 1)
                            }
                        }
                    )

                    if (editingField == EditingDateField.EXACT_DAY) {
                        Spacer(modifier = Modifier.height(12.dp))

                        MiniCalendarCard(
                            visibleMonth = visibleMonth,
                            startDate = exactDay,
                            endDate = exactDay,
                            editingField = editingField,
                            onPrevMonth = {
                                visibleMonth = (visibleMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, -1)
                                }
                            },
                            onNextMonth = {
                                visibleMonth = (visibleMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, 1)
                                }
                            },
                            onDateSelected = { selected ->
                                val normalizedSelected = clearTime(selected.clone() as Calendar)

                                exactDay = normalizedSelected
                                onDaySelected(normalizedSelected.clone() as Calendar)
                            }
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Text(
                        text = "Быстрый выбор периода",
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickPeriodButton(
                            text = "7 дней",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val today = clearTime(Calendar.getInstance())

                                startDate = today
                                endDate = (today.clone() as Calendar).apply {
                                    add(Calendar.DAY_OF_MONTH, 7)
                                }

                                visibleMonth = (startDate.clone() as Calendar).apply {
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }

                                editingField = null
                            }
                        )

                        QuickPeriodButton(
                            text = "30 дней",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val today = clearTime(Calendar.getInstance())

                                startDate = today
                                endDate = (today.clone() as Calendar).apply {
                                    add(Calendar.DAY_OF_MONTH, 30)
                                }

                                visibleMonth = (startDate.clone() as Calendar).apply {
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }

                                editingField = null
                            }
                        )

                        QuickPeriodButton(
                            text = "90 дней",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val today = clearTime(Calendar.getInstance())

                                startDate = today
                                endDate = (today.clone() as Calendar).apply {
                                    add(Calendar.DAY_OF_MONTH, 90)
                                }

                                visibleMonth = (startDate.clone() as Calendar).apply {
                                    set(Calendar.DAY_OF_MONTH, 1)
                                }

                                editingField = null
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Или выберите даты периода",
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PeriodDateField(
                        label = "Начало периода",
                        value = dateFormatter.format(startDate.time),
                        active = editingField == EditingDateField.START,
                        onClick = {
                            editingField = EditingDateField.START
                            visibleMonth = (startDate.clone() as Calendar).apply {
                                set(Calendar.DAY_OF_MONTH, 1)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PeriodDateField(
                        label = "Конец периода",
                        value = dateFormatter.format(endDate.time),
                        active = editingField == EditingDateField.END,
                        onClick = {
                            editingField = EditingDateField.END
                            visibleMonth = (endDate.clone() as Calendar).apply {
                                set(Calendar.DAY_OF_MONTH, 1)
                            }
                        }
                    )

                    if (editingField == EditingDateField.START || editingField == EditingDateField.END) {
                        Spacer(modifier = Modifier.height(12.dp))

                        MiniCalendarCard(
                            visibleMonth = visibleMonth,
                            startDate = startDate,
                            endDate = endDate,
                            editingField = editingField,
                            onPrevMonth = {
                                visibleMonth = (visibleMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, -1)
                                }
                            },
                            onNextMonth = {
                                visibleMonth = (visibleMonth.clone() as Calendar).apply {
                                    add(Calendar.MONTH, 1)
                                }
                            },
                            onDateSelected = { selected ->
                                val normalizedSelected = clearTime(selected.clone() as Calendar)

                                when (editingField) {
                                    EditingDateField.START -> {
                                        startDate = normalizedSelected

                                        if (startDate.after(endDate)) {
                                            endDate = startDate.clone() as Calendar
                                        }
                                    }

                                    EditingDateField.END -> {
                                        endDate = normalizedSelected

                                        if (endDate.before(startDate)) {
                                            startDate = endDate.clone() as Calendar
                                        }
                                    }

                                    EditingDateField.EXACT_DAY,
                                    null -> Unit
                                }

                                editingField = null
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onApplyPeriod(
                                startDate.clone() as Calendar,
                                endDate.clone() as Calendar
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.surface
                        )
                    ) {
                        Text(
                            text = "Показать аналитику за период",
                            style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JumpToDayCard(
    dateText: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isActive) {
                    colors.primary.copy(alpha = 0.10f)
                } else {
                    colors.borderLight
                }
            )
            .border(
                width = 1.dp,
                color = if (isActive) {
                    colors.primary
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = "Перейти к нужному дню",
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )

            Text(
                text = "Выберите дату, и откроется вкладка «День»",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Text(
            text = dateText,
            style = typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = colors.primary
        )
    }
}

@Composable
private fun QuickPeriodButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colors.borderLight)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = colors.textPrimary
        )
    }
}

@Composable
private fun PeriodDateField(
    label: String,
    value: String,
    active: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column {
        Text(
            text = label,
            style = typography.bodySmall,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface)
                .border(
                    width = 1.dp,
                    color = if (active) colors.primary else colors.borderMedium,
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable { onClick() }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = typography.bodyMedium,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                tint = if (active) colors.primary else colors.textSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MiniCalendarCard(
    visibleMonth: Calendar,
    startDate: Calendar,
    endDate: Calendar,
    editingField: EditingDateField?,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (Calendar) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val monthTitleFormatter = remember {
        SimpleDateFormat("LLLL yyyy", Locale("ru", "RU"))
    }

    val days by remember(visibleMonth.timeInMillis) {
        derivedStateOf {
            buildMiniCalendarDays(visibleMonth)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colors.borderLight)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalendarSmallArrow(
                text = "‹",
                onClick = onPrevMonth
            )

            Text(
                text = monthTitleFormatter.format(visibleMonth.time)
                    .replaceFirstChar { it.uppercase() },
                style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.textPrimary
            )

            CalendarSmallArrow(
                text = "›",
                onClick = onNextMonth
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                Box(
                    modifier = Modifier.size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = typography.bodySmall,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            days.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week.forEach { date ->
                        MiniCalendarDayCell(
                            date = date,
                            startDate = startDate,
                            endDate = endDate,
                            editingField = editingField,
                            onDateSelected = onDateSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniCalendarDayCell(
    date: Calendar?,
    startDate: Calendar,
    endDate: Calendar,
    editingField: EditingDateField?,
    onDateSelected: (Calendar) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    if (date == null) {
        Box(modifier = Modifier.size(36.dp))
        return
    }

    val isStart = isSameDay(date, startDate)
    val isEnd = isSameDay(date, endDate)
    val inRange = isDateInRange(date, startDate, endDate)
    val isExactDay = editingField == EditingDateField.EXACT_DAY && isStart

    val isActiveEditTarget =
        isExactDay ||
                (editingField == EditingDateField.START && isStart) ||
                (editingField == EditingDateField.END && isEnd)

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isStart || isEnd -> colors.primary
                    inRange -> colors.primary.copy(alpha = 0.12f)
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (isActiveEditTarget) 1.dp else 0.dp,
                color = if (isActiveEditTarget) colors.primary else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.get(Calendar.DAY_OF_MONTH).toString(),
            style = typography.bodySmall.copy(
                fontWeight = if (isStart || isEnd) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            ),
            color = if (isStart || isEnd) {
                colors.surface
            } else {
                colors.textPrimary
            }
        )
    }
}

@Composable
private fun CalendarSmallArrow(
    text: String,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = typography.titleMedium,
            color = colors.textPrimary
        )
    }
}

private fun buildMiniCalendarDays(monthCalendar: Calendar): List<Calendar?> {
    val firstDayOfMonth = (monthCalendar.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)

    val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK)
    val leadingEmptyDays = when (firstDayOfWeek) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        Calendar.SUNDAY -> 6
        else -> 0
    }

    val result = mutableListOf<Calendar?>()

    repeat(leadingEmptyDays) {
        result.add(null)
    }

    for (day in 1..daysInMonth) {
        val date = (firstDayOfMonth.clone() as Calendar).apply {
            set(Calendar.DAY_OF_MONTH, day)
        }

        result.add(date)
    }

    while (result.size % 7 != 0) {
        result.add(null)
    }

    return result
}

private fun isSameDay(
    first: Calendar,
    second: Calendar
): Boolean {
    return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
            first.get(Calendar.MONTH) == second.get(Calendar.MONTH) &&
            first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH)
}

private fun isDateInRange(
    date: Calendar,
    startDate: Calendar,
    endDate: Calendar
): Boolean {
    val normalizedDate = clearTime(date.clone() as Calendar)
    val normalizedStart = clearTime(startDate.clone() as Calendar)
    val normalizedEnd = clearTime(endDate.clone() as Calendar)

    return !normalizedDate.before(normalizedStart) &&
            !normalizedDate.after(normalizedEnd)
}

private fun clearTime(calendar: Calendar): Calendar {
    return calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}