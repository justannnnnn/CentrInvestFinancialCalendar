package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sdk.domain.model.DayData
import com.example.sdk.domain.model.Transaction
import com.example.sdk.presentation.models.ViewModeTab
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
data class CalendarUiState(
    val selectedPeriod: Calendar = Calendar.getInstance(),
    val selectedDate: Calendar? = null,
    val daysData: Map<Int, DayData> = emptyMap(),
    val viewModeTabs: List<ViewModeTab> =
        listOf(ViewModeTab.Month, ViewModeTab.Week, ViewModeTab.Day),
    val selectedViewMode: ViewModeTab = ViewModeTab.Month,
    val showBottomSheet: Boolean = false
) {
    val allMonthTransactions: List<Transaction>
        get() = daysData.values.flatMap { it.transactions }.filter { it.amount != 0L }
}

sealed interface CalendarUiAction {
    data object OnPrevPeriodClick : CalendarUiAction
    data object OnNextPeriodClick : CalendarUiAction

    data class OnDaySelected(val day: Int) : CalendarUiAction

    data class OnViewModeSelected(val mode: ViewModeTab) : CalendarUiAction

    data object OnDismissBottomSheet : CalendarUiAction
    data object OnAddClick : CalendarUiAction
}

sealed interface CalendarSideEffect {
    data object OpenAddScreen : CalendarSideEffect
}