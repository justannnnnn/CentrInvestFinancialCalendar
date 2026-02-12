package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.sdk.domain.model.DayData
import com.example.sdk.domain.model.Transaction
import com.example.sdk.presentation.models.ViewModeTab
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
data class CalendarUiState(
    val selectedMonth: Calendar = Calendar.getInstance(),
    val selectedDate: LocalDate? = null,
    val daysData: Map<Int, DayData> = emptyMap(),
    val viewModeTabs: List<ViewModeTab> =
        listOf(ViewModeTab.Month, ViewModeTab.Week, ViewModeTab.Day),
    val selectedViewMode: ViewModeTab = ViewModeTab.Month,
    val showBottomSheet: Boolean = false
) {
    val allMonthTransactions: List<Transaction>
        get() = daysData.values.flatMap { it.transactions }
}

sealed interface CalendarUiAction {
    data object OnPrevMonthClick : CalendarUiAction
    data object OnNextMonthClick : CalendarUiAction

    data class OnDaySelected(val day: Int) : CalendarUiAction

    data class OnViewModeSelected(val mode: ViewModeTab) : CalendarUiAction

    data object OnDismissBottomSheet : CalendarUiAction
    data object OnAddClick : CalendarUiAction
}

sealed interface CalendarSideEffect {
    data object OpenAddScreen : CalendarSideEffect
}