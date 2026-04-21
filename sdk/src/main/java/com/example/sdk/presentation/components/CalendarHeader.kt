package com.example.sdk.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sdk.R
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.presentation.models.ThemeSelection
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.GraphitePrimary
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.OceanPrimary
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarHeader(
    calendar: Calendar,
    selectedViewMode: ViewModeTab,
    activeThemeSelection: ThemeSelection,
    activeThemePrimaryColor: Color,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onThemeClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
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
                        ViewModeTab.Month -> {
                            "${getMonthNameNominative(calendar)} ${calendar.get(Calendar.YEAR)}"
                        }
                    },
                    onPeriodSelectorClick = {}
                )

                ArrowButton(
                    isLeft = false,
                    onClick = onNextMonth
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThemeSwitcherButton(
                    activeThemeSelection = activeThemeSelection,
                    activeThemePrimaryColor = activeThemePrimaryColor,
                    onClick = onThemeClick
                )
                PlusButton(onClick = onAddClick)
            }
        }
    }
}

@Composable
private fun ThemeSwitcherButton(
    activeThemeSelection: ThemeSelection,
    activeThemePrimaryColor: Color,
    onClick: () -> Unit
) {
    val activeColor = when (activeThemeSelection) {
        is ThemeSelection.Preset -> {
            when (activeThemeSelection.preset) {
                CalendarThemePreset.Default -> GreenPrimary
                CalendarThemePreset.Ocean -> OceanPrimary
                CalendarThemePreset.Graphite -> GraphitePrimary
            }
        }

        is ThemeSelection.Custom -> activeThemePrimaryColor
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(activeColor.copy(alpha = 0.16f))
            .border(
                width = 1.dp,
                color = activeColor.copy(alpha = 0.28f),
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        ThemeWandGlyph(accentColor = activeColor)
    }
}

@Composable
private fun ThemeWandGlyph(
    accentColor: Color
) {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Палочка
        Box(
            modifier = Modifier
                .size(width = 18.dp, height = 4.dp)
                .rotate(-45f)
                .offset(x = 2.dp, y = 4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colors.textPrimary.copy(alpha = 0.78f))
        )

        // Наконечник
        Box(
            modifier = Modifier
                .size(10.dp)
                .rotate(45f)
                .offset(x = (-4).dp, y = (-5).dp)
                .clip(RoundedCornerShape(3.dp))
                .background(accentColor)
                .border(
                    width = 1.dp,
                    color = colors.surface.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(3.dp)
                )
        )

        // Блестка
        Box(
            modifier = Modifier
                .size(5.dp)
                .offset(x = 6.dp, y = (-7).dp)
                .clip(CircleShape)
                .background(colors.surface)
        )

        Box(
            modifier = Modifier
                .size(width = 2.dp, height = 7.dp)
                .offset(x = 6.dp, y = (-7).dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colors.surface)
        )

        Box(
            modifier = Modifier
                .size(width = 7.dp, height = 2.dp)
                .offset(x = 6.dp, y = (-7).dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colors.surface)
        )
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.primary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        IconWrapper(
            modifier = Modifier.size(28.dp),
            iconRes = icons.add,
            color = colors.surface
        )
    }
}

@Composable
private fun PeriodSelector(
    title: String,
    onPeriodSelectorClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

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
            style = typography.bodyLarge,
            color = colors.textPrimary
        )

        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                modifier = Modifier.fillMaxSize(),
                iconRes = R.drawable.calendar,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun ArrowButton(
    isLeft: Boolean,
    onClick: () -> Unit
) {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val iconRes = if (isLeft) icons.arrowLeft else icons.arrowRight

        IconWrapper(
            iconRes = iconRes,
            color = colors.textTertiary,
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

    calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startDay = dayFormat.format(calendarCopy.time)

    calendarCopy.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    val endDay = dayFormat.format(calendarCopy.time)
    val monthYear = monthFormat.format(calendarCopy.time)

    return "$startDay - $endDay $monthYear"
}

private fun getDayDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
    return dateFormat.format(calendar.time)
}