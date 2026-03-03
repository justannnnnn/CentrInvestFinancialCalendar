package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sdk.presentation.models.ViewModeTab
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class CalendarUiState(
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val viewModeTabs: List<ViewModeTab> =
        listOf(ViewModeTab.Month, ViewModeTab.Week, ViewModeTab.Day),
    val selectedViewMode: ViewModeTab = ViewModeTab.Month,

    //  период для статистики/фильтра
    val periodStart: LocalDate = LocalDate.now().minusDays(30),
    val periodEnd: LocalDate = LocalDate.now()
)