package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.domain.model.DayData
import com.example.sdk.presentation.models.ViewModeTab
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
data class CalendarUiState(
    val selectedPeriod: Calendar = Calendar.getInstance(),
    val selectedDate: Calendar? = null,
    val daysData: Map<Int, DayData> = emptyMap(),
    val categories: List<CalendarCategoryUi> = emptyList(),
    val allOperations: List<CalendarOperationUi> = emptyList(),

    val viewModeTabs: List<ViewModeTab> =
        listOf(ViewModeTab.Month, ViewModeTab.Week, ViewModeTab.Day),
    val selectedViewMode: ViewModeTab = ViewModeTab.Month,

    val showBottomSheet: Boolean = false,
    val isAddTransactionVisible: Boolean = false,

    val editingOperation: CalendarOperationUi? = null,
    val pendingDeleteOperation: CalendarOperationUi? = null,

    val isSavingOperation: Boolean = false,
    val isDeletingOperation: Boolean = false,

    val addOperationError: String? = null,
    val deleteOperationError: String? = null,

    val periodAnalyticsStart: Calendar? = null,
    val periodAnalyticsEnd: Calendar? = null,
    val isPeriodAnalyticsVisible: Boolean = false
) {
    val allMonthTransactions: List<CalendarOperationUi>
        get() = daysData.values.flatMap { it.operations }
}

sealed interface CalendarUiAction {
    data object OnPrevPeriodClick : CalendarUiAction
    data object OnNextPeriodClick : CalendarUiAction

    data class OnDaySelected(val day: Int) : CalendarUiAction

    data class OnPeriodSelected(
        val calendar: Calendar
    ) : CalendarUiAction

    data class OnExactDaySelected(
        val calendar: Calendar
    ) : CalendarUiAction

    data class OnAnalyticsPeriodSelected(
        val startDate: Calendar,
        val endDate: Calendar
    ) : CalendarUiAction

    data object OnDismissPeriodAnalytics : CalendarUiAction

    data class OnViewModeSelected(val mode: ViewModeTab) : CalendarUiAction

    data class OnEditOperationClick(
        val operation: CalendarOperationUi
    ) : CalendarUiAction

    data class OnDeleteOperationClick(
        val operation: CalendarOperationUi
    ) : CalendarUiAction

    data object OnConfirmDeleteOperation : CalendarUiAction
    data object OnCancelDeleteOperation : CalendarUiAction

    data object OnDismissBottomSheet : CalendarUiAction
    data object OnAddClick : CalendarUiAction
}

sealed interface CalendarSideEffect {
    data object OpenAddScreen : CalendarSideEffect
}