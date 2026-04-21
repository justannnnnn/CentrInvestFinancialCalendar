package com.example.sdk.presentation.themeeditor

import androidx.annotation.DrawableRes
import com.example.sdk.R
import com.example.sdk.sdk.CalendarIconOverrides

enum class ThemeIconFieldKey {
    AddIcon,
    MonthTabIcon,
    WeekTabIcon,
    DayTabIcon,
    CalendarBottomNavIcon,
    ArrowLeftIcon,
    ArrowRightIcon,
    RecurringIcon
}

data class ThemeIconField(
    val key: ThemeIconFieldKey,
    val title: String
)

data class AvailableIconOption(
    @DrawableRes val resId: Int,
    val title: String
)

object ThemeIconFields {

    val all: List<ThemeIconField> = listOf(
        ThemeIconField(
            key = ThemeIconFieldKey.AddIcon,
            title = "Иконка добавления"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.MonthTabIcon,
            title = "Иконка вкладки Month"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.WeekTabIcon,
            title = "Иконка вкладки Week"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.DayTabIcon,
            title = "Иконка вкладки Day"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.CalendarBottomNavIcon,
            title = "Иконка нижней навигации"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.ArrowLeftIcon,
            title = "Левая стрелка"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.ArrowRightIcon,
            title = "Правая стрелка"
        ),
        ThemeIconField(
            key = ThemeIconFieldKey.RecurringIcon,
            title = "Иконка регулярного платежа"
        )
    )

    val availableIcons: List<AvailableIconOption> = listOf(
        AvailableIconOption(R.drawable.plus, "Plus"),
        AvailableIconOption(R.drawable.ic_month, "Month"),
        AvailableIconOption(R.drawable.ic_week, "Week"),
        AvailableIconOption(R.drawable.ic_day, "Day"),
        AvailableIconOption(R.drawable.calendar, "Calendar"),
        AvailableIconOption(R.drawable.chevron_left, "Left"),
        AvailableIconOption(R.drawable.chevron_right, "Right"),
        AvailableIconOption(R.drawable.calendar, "Recurring")
    )
}

fun CalendarIconOverrides.getIconValue(
    key: ThemeIconFieldKey
): Int? {
    return when (key) {
        ThemeIconFieldKey.AddIcon -> addIconRes
        ThemeIconFieldKey.MonthTabIcon -> monthTabIconRes
        ThemeIconFieldKey.WeekTabIcon -> weekTabIconRes
        ThemeIconFieldKey.DayTabIcon -> dayTabIconRes
        ThemeIconFieldKey.CalendarBottomNavIcon -> calendarBottomNavIconRes
        ThemeIconFieldKey.ArrowLeftIcon -> arrowLeftIconRes
        ThemeIconFieldKey.ArrowRightIcon -> arrowRightIconRes
        ThemeIconFieldKey.RecurringIcon -> recurringIconRes
    }
}

fun CalendarIconOverrides.withIconValue(
    key: ThemeIconFieldKey,
    iconRes: Int?
): CalendarIconOverrides {
    return when (key) {
        ThemeIconFieldKey.AddIcon -> copy(addIconRes = iconRes)
        ThemeIconFieldKey.MonthTabIcon -> copy(monthTabIconRes = iconRes)
        ThemeIconFieldKey.WeekTabIcon -> copy(weekTabIconRes = iconRes)
        ThemeIconFieldKey.DayTabIcon -> copy(dayTabIconRes = iconRes)
        ThemeIconFieldKey.CalendarBottomNavIcon -> copy(calendarBottomNavIconRes = iconRes)
        ThemeIconFieldKey.ArrowLeftIcon -> copy(arrowLeftIconRes = iconRes)
        ThemeIconFieldKey.ArrowRightIcon -> copy(arrowRightIconRes = iconRes)
        ThemeIconFieldKey.RecurringIcon -> copy(recurringIconRes = iconRes)
    }
}