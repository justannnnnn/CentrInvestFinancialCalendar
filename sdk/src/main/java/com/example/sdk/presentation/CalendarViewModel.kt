package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdk.data.network.dto.Recurrence
import com.example.sdk.data.network.dto.RecurrenceUnit
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.domain.model.DayData
import com.example.sdk.domain.repository.TransactionsRepository
import com.example.sdk.presentation.models.ViewModeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TransactionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val _sideEffect = MutableSharedFlow<CalendarSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var domainOperations: List<CalendarOperationUi> = emptyList()
    private var domainCategories: List<CalendarCategoryUi> = emptyList()

    init {
        loadStartData()
    }

    private fun loadStartData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repo.getCalendarData()
                }

                domainCategories = data.categories.map { it.toUi() }
                domainOperations = data.operations.map { it.toUi() }

                _uiState.update {
                    it.copy(
                        categories = domainCategories,
                        allOperations = domainOperations
                    )
                }

                rebuildMonth()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun rebuildMonth() {
        val month = uiState.value.selectedPeriod
        val days = buildCalendar(
            operations = domainOperations,
            monthCalendar = month
        )

        _uiState.update {
            it.copy(daysData = days)
        }
    }

    private fun buildCalendar(
        operations: List<CalendarOperationUi>,
        monthCalendar: Calendar
    ): Map<Int, DayData> {
        val year = monthCalendar.get(Calendar.YEAR)
        val month = monthCalendar.get(Calendar.MONTH)

        val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val calendarMap = (1..daysInMonth).associateWith { day ->
            DayData(
                day = day,
                operations = emptyList(),
                hasRecurring = false
            )
        }.toMutableMap()

        operations.forEach { operation ->
            val operationCalendar = Calendar.getInstance().apply {
                timeInMillis = operation.dateTime
            }

            if (
                operationCalendar.get(Calendar.YEAR) == year &&
                operationCalendar.get(Calendar.MONTH) == month
            ) {
                val day = operationCalendar.get(Calendar.DAY_OF_MONTH)
                val oldDayData = calendarMap[day]

                if (oldDayData != null) {
                    calendarMap[day] = oldDayData.copy(
                        operations = oldDayData.operations + operation,
                        hasRecurring = oldDayData.hasRecurring || (operation.recurrence != null)
                    )
                }
            }

            operation.recurrence?.let { recurrence ->
                val recurrenceCalendar = operationCalendar.clone() as Calendar

                val until = Calendar.getInstance().apply {
                    add(Calendar.YEAR, 2)
                }

                addByRecurrence(
                    calendar = recurrenceCalendar,
                    recurrence = recurrence
                )

                while (!recurrenceCalendar.after(until)) {
                    if (
                        recurrenceCalendar.get(Calendar.YEAR) == year &&
                        recurrenceCalendar.get(Calendar.MONTH) == month
                    ) {
                        val day = recurrenceCalendar.get(Calendar.DAY_OF_MONTH)
                        val oldDayData = calendarMap[day]

                        if (oldDayData != null) {
                            calendarMap[day] = oldDayData.copy(
                                operations = oldDayData.operations + operation,
                                hasRecurring = true
                            )
                        }
                    }

                    addByRecurrence(
                        calendar = recurrenceCalendar,
                        recurrence = recurrence
                    )
                }
            }
        }

        return calendarMap
    }

    private fun addByRecurrence(
        calendar: Calendar,
        recurrence: Recurrence
    ) {
        when (recurrence.unit) {
            RecurrenceUnit.DAY -> {
                calendar.add(Calendar.DAY_OF_MONTH, recurrence.every)
            }

            RecurrenceUnit.WEEK -> {
                calendar.add(Calendar.WEEK_OF_YEAR, recurrence.every)
            }

            RecurrenceUnit.MONTH -> {
                calendar.add(Calendar.MONTH, recurrence.every)
            }
        }
    }

    fun onAction(action: CalendarUiAction) {
        when (action) {
            CalendarUiAction.OnPrevPeriodClick -> {
                val interval = getCurrentPeriodInterval()

                val newPeriod = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                    add(interval, -1)
                }

                _uiState.update {
                    it.copy(
                        selectedPeriod = newPeriod,
                        selectedDate = newPeriod.clone() as Calendar,
                        showBottomSheet = false
                    )
                }

                rebuildMonth()
            }

            CalendarUiAction.OnNextPeriodClick -> {
                val interval = getCurrentPeriodInterval()

                val newPeriod = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                    add(interval, 1)
                }

                _uiState.update {
                    it.copy(
                        selectedPeriod = newPeriod,
                        selectedDate = newPeriod.clone() as Calendar,
                        showBottomSheet = false
                    )
                }

                rebuildMonth()
            }

            is CalendarUiAction.OnDaySelected -> {
                val selectedDate = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_MONTH, action.day)
                }

                _uiState.update {
                    it.copy(
                        selectedDate = selectedDate,
                        showBottomSheet = true
                    )
                }
            }

            is CalendarUiAction.OnPeriodSelected -> {
                val selectedCalendar = action.calendar.clone() as Calendar

                _uiState.update {
                    it.copy(
                        selectedPeriod = selectedCalendar,
                        selectedDate = selectedCalendar.clone() as Calendar,
                        showBottomSheet = false
                    )
                }

                rebuildMonth()
            }

            is CalendarUiAction.OnExactDaySelected -> {
                val selectedCalendar = action.calendar.clone() as Calendar

                _uiState.update {
                    it.copy(
                        selectedPeriod = selectedCalendar,
                        selectedDate = selectedCalendar.clone() as Calendar,
                        selectedViewMode = ViewModeTab.Day,
                        showBottomSheet = false
                    )
                }

                rebuildMonth()
            }

            is CalendarUiAction.OnAnalyticsPeriodSelected -> {
                val start = action.startDate.clone() as Calendar
                val end = action.endDate.clone() as Calendar

                _uiState.update {
                    it.copy(
                        periodAnalyticsStart = start,
                        periodAnalyticsEnd = end,
                        isPeriodAnalyticsVisible = true,
                        showBottomSheet = false
                    )
                }
            }

            CalendarUiAction.OnDismissPeriodAnalytics -> {
                _uiState.update {
                    it.copy(
                        isPeriodAnalyticsVisible = false,
                        periodAnalyticsStart = null,
                        periodAnalyticsEnd = null
                    )
                }
            }

            is CalendarUiAction.OnViewModeSelected -> {
                _uiState.update {
                    it.copy(
                        selectedViewMode = action.mode,
                        showBottomSheet = false
                    )
                }
            }

            CalendarUiAction.OnDismissBottomSheet -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        selectedDate = null
                    )
                }
            }

            CalendarUiAction.OnAddClick -> {
                _uiState.update {
                    it.copy(
                        editingOperation = null,
                        isAddTransactionVisible = true,
                        addOperationError = null
                    )
                }
            }

            is CalendarUiAction.OnEditOperationClick -> {
                _uiState.update {
                    it.copy(
                        editingOperation = action.operation,
                        isAddTransactionVisible = true,
                        showBottomSheet = false,
                        addOperationError = null
                    )
                }
            }

            is CalendarUiAction.OnDeleteOperationClick -> {
                _uiState.update {
                    it.copy(
                        pendingDeleteOperation = action.operation,
                        deleteOperationError = null
                    )
                }
            }

            CalendarUiAction.OnConfirmDeleteOperation -> {
                val operation = _uiState.value.pendingDeleteOperation

                if (operation != null) {
                    deleteOperation(operation)
                }
            }

            CalendarUiAction.OnCancelDeleteOperation -> {
                _uiState.update {
                    it.copy(
                        pendingDeleteOperation = null,
                        deleteOperationError = null
                    )
                }
            }
        }
    }

    private fun getCurrentPeriodInterval(): Int {
        return when (_uiState.value.selectedViewMode) {
            ViewModeTab.Day -> Calendar.DAY_OF_MONTH
            ViewModeTab.Month -> Calendar.MONTH
            ViewModeTab.Week -> Calendar.WEEK_OF_YEAR
        }
    }

    fun onAddDismiss() {
        _uiState.update {
            it.copy(
                isAddTransactionVisible = false,
                editingOperation = null,
                addOperationError = null
            )
        }
    }

    fun saveOperation(operation: CalendarOperationUi) {
        if (_uiState.value.isSavingOperation) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSavingOperation = true,
                    addOperationError = null
                )
            }

            try {
                val editingOperation = _uiState.value.editingOperation

                val operationToSave = if (editingOperation != null) {
                    operation.copy(
                        id = editingOperation.id,
                        isCustom = editingOperation.isCustom,
                        status = editingOperation.status
                    )
                } else {
                    operation
                }

                val dtoOperation = operationToSave.toDto()

                withContext(Dispatchers.IO) {
                    if (editingOperation != null) {
                        repo.updateOperation(dtoOperation)
                    } else {
                        repo.addOperation(dtoOperation)
                    }
                }

                _uiState.update {
                    it.copy(
                        isAddTransactionVisible = false,
                        editingOperation = null,
                        isSavingOperation = false,
                        addOperationError = null
                    )
                }

                loadStartData()
            } catch (e: Exception) {
                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isSavingOperation = false,
                        addOperationError = e.message ?: "Не удалось сохранить операцию"
                    )
                }
            }
        }
    }

    private fun deleteOperation(operation: CalendarOperationUi) {
        if (_uiState.value.isDeletingOperation) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDeletingOperation = true,
                    deleteOperationError = null
                )
            }

            try {
                withContext(Dispatchers.IO) {
                    repo.deleteOperation(operation.id)
                }

                domainOperations = domainOperations.filterNot {
                    it.id == operation.id
                }

                _uiState.update {
                    it.copy(
                        allOperations = domainOperations
                    )
                }

                rebuildMonth()

                _uiState.update {
                    it.copy(
                        isDeletingOperation = false,
                        pendingDeleteOperation = null,
                        deleteOperationError = null
                    )
                }

                loadStartData()
            } catch (e: Exception) {
                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isDeletingOperation = false,
                        deleteOperationError = e.message ?: "Не удалось удалить операцию"
                    )
                }
            }
        }
    }
}