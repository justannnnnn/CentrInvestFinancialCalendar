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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.sdk.R
import com.example.sdk.presentation.models.ThemeSelection
import com.example.sdk.presentation.models.ViewModeTab
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
    onPeriodClick: () -> Unit,
    onThemeClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val colors = CalendarTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(top = 8.dp, start = 12.dp, end = 12.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PeriodNavigationChip(
            title = when (selectedViewMode) {
                ViewModeTab.Day -> getDayDate(calendar)
                ViewModeTab.Week -> getWeekRange(calendar)
                ViewModeTab.Month -> {
                    "${getMonthNameNominative(calendar)} ${calendar.get(Calendar.YEAR)}"
                }
            },
            onPrevClick = onPrevMonth,
            onNextClick = onNextMonth,
            onPeriodClick = onPeriodClick,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
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

@Composable
private fun PeriodNavigationChip(
    title: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onPeriodClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = modifier
            .height(48.dp)
            .widthIn(min = 150.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(colors.background)
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderSmallIconButton(
            onClick = onPrevClick
        ) {
            val icons = CalendarTheme.icons

            IconWrapper(
                iconRes = icons.arrowLeft,
                color = colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable { onPeriodClick() }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconWrapper(
                    modifier = Modifier.fillMaxSize(),
                    iconRes = R.drawable.calendar,
                    color = colors.textSecondary
                )
            }
        }

        HeaderSmallIconButton(
            onClick = onNextClick
        ) {
            val icons = CalendarTheme.icons

            IconWrapper(
                iconRes = icons.arrowRight,
                color = colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun HeaderSmallIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = CalendarTheme.colors

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.surface),
            contentAlignment = Alignment.Center
        ) {
            content()
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
            .size(42.dp)
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
        Box(
            modifier = Modifier
                .size(width = 18.dp, height = 4.dp)
                .rotate(-45f)
                .offset(x = 2.dp, y = 4.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(colors.textPrimary.copy(alpha = 0.78f))
        )

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

fun getMonthNameNominative(
    calendar: Calendar,
    locale: Locale = Locale("ru", "RU")
): String {
    val monthIndex = calendar.get(Calendar.MONTH)

    val ruMonths = arrayOf(
        "январь",
        "февраль",
        "март",
        "апрель",
        "май",
        "июнь",
        "июль",
        "август",
        "сентябрь",
        "октябрь",
        "ноябрь",
        "декабрь"
    )

    val name = if (locale.language == "ru") {
        ruMonths[monthIndex]
    } else {
        DateFormatSymbols(locale).months[monthIndex]
    }

    return name.replaceFirstChar { it.uppercase(locale) }
}

private fun getWeekRange(calendar: Calendar): String {
    val start = (calendar.clone() as Calendar).apply {
        firstDayOfWeek = Calendar.MONDAY

        while (get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            add(Calendar.DAY_OF_MONTH, -1)
        }
    }

    val end = (start.clone() as Calendar).apply {
        add(Calendar.DAY_OF_MONTH, 6)
    }

    val startDay = start.get(Calendar.DAY_OF_MONTH)
    val endDay = end.get(Calendar.DAY_OF_MONTH)

    val startMonth = getMonthNameGenitive(start)
    val endMonth = getMonthNameGenitive(end)

    val startYear = start.get(Calendar.YEAR)
    val endYear = end.get(Calendar.YEAR)

    return when {
        startYear != endYear -> {
            "$startDay $startMonth $startYear - $endDay $endMonth $endYear"
        }

        start.get(Calendar.MONTH) != end.get(Calendar.MONTH) -> {
            "$startDay $startMonth - $endDay $endMonth $endYear"
        }

        else -> {
            "$startDay - $endDay $endMonth $endYear"
        }
    }
}

private fun getDayDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
    return dateFormat.format(calendar.time)
}

private fun getMonthNameGenitive(calendar: Calendar): String {
    val months = arrayOf(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
    )

    return months[calendar.get(Calendar.MONTH)]
}