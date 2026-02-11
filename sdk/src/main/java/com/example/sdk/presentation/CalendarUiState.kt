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
    val selectedViewMode: ViewModeTab = ViewModeTab.Month
)