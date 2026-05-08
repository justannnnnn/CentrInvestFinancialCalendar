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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TransactionsRepository
) : ViewModel() {

    init {
        loadStartData()
    }

    private var domainOperations: List<CalendarOperationUi> = emptyList()
    private var domainCategories: List<CalendarCategoryUi> = emptyList()

    private fun loadStartData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repo.getCalendarData()
                }
                
                domainOperations = data.operations.map { it.toUi() }
                domainCategories = data.categories.map { it.toUi() }

                _uiState.update { it.copy(categories = domainCategories) }
                rebuildMonth()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun rebuildMonth() {
        val month = uiState.value.selectedPeriod
        val days = buildCalendar(domainOperations, month)

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
        val calendarMap = (1..daysInMonth).associateWith {
            DayData(day = it, operations = emptyList(), hasRecurring = false)
        }.toMutableMap()

        operations.forEach { op ->
            val opCal = Calendar.getInstance().apply { timeInMillis = op.dateTime }

            if (opCal.get(Calendar.YEAR) == year && opCal.get(Calendar.MONTH) == month) {
                val day = opCal.get(Calendar.DAY_OF_MONTH)
                val old = calendarMap[day]!!
                calendarMap[day] = old.copy(
                    operations = old.operations + op,
                    hasRecurring = old.hasRecurring || (op.recurrence != null)
                )
            }

            op.recurrence?.let { rule ->
                val recurCal = opCal.clone() as Calendar
                val until = Calendar.getInstance().apply { add(Calendar.YEAR, 2) }
                
                addByRecurrence(recurCal, rule)

                while (!recurCal.after(until)) {
                    if (recurCal.get(Calendar.YEAR) == year && recurCal.get(Calendar.MONTH) == month) {
                        val day = recurCal.get(Calendar.DAY_OF_MONTH)
                        val old = calendarMap[day]!!
                        calendarMap[day] = old.copy(
                            operations = old.operations + op,
                            hasRecurring = true
                        )
                    }
                    addByRecurrence(recurCal, rule)
                }
            }
        }

        return calendarMap
    }

    private fun addByRecurrence(cal: Calendar, recurrence: Recurrence) {
        when (recurrence.unit) {
            RecurrenceUnit.DAY -> cal.add(Calendar.DAY_OF_MONTH, recurrence.every)
            RecurrenceUnit.WEEK -> cal.add(Calendar.WEEK_OF_YEAR, recurrence.every)
            RecurrenceUnit.MONTH -> cal.add(Calendar.MONTH, recurrence.every)
        }
    }

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val _sideEffect = MutableSharedFlow<CalendarSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onAction(action: CalendarUiAction) {
        when (action) {
            CalendarUiAction.OnPrevPeriodClick -> {
                val interval = when (_uiState.value.selectedViewMode) {
                    ViewModeTab.Day -> Calendar.DAY_OF_MONTH
                    ViewModeTab.Month -> Calendar.MONTH
                    ViewModeTab.Week -> Calendar.WEEK_OF_MONTH
                }
                val newPeriod = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                    add(interval, -1)
                }
                _uiState.update { it.copy(selectedPeriod = newPeriod) }
                rebuildMonth()
            }

            CalendarUiAction.OnNextPeriodClick -> {
                val interval = when (_uiState.value.selectedViewMode) {
                    ViewModeTab.Day -> Calendar.DAY_OF_MONTH
                    ViewModeTab.Month -> Calendar.MONTH
                    ViewModeTab.Week -> Calendar.WEEK_OF_MONTH
                }
                val newPeriod = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                    add(interval, 1)
                }
                _uiState.update { it.copy(selectedPeriod = newPeriod) }
                rebuildMonth()
            }

            is CalendarUiAction.OnDaySelected -> {
                _uiState.update {
                    it.copy(
                        selectedDate = (_uiState.value.selectedPeriod.clone() as Calendar).apply {
                            set(Calendar.DAY_OF_MONTH, action.day)
                        },
                        showBottomSheet = true
                    )
                }
            }

            is CalendarUiAction.OnViewModeSelected -> {
                _uiState.update { it.copy(selectedViewMode = action.mode) }
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
                _uiState.update { it.copy(isAddTransactionVisible = true) }
            }
        }
    }

    fun onAddDismiss() {
        _uiState.update { it.copy(isAddTransactionVisible = false) }
    }

    fun saveOperation(operation: CalendarOperationUi) {
        viewModelScope.launch {
            try {
                // Mapping Domain back to DTO for saving
                val dtoOp = operation.toDto()
                withContext(Dispatchers.IO) {
                    repo.addOperation(dtoOp)
                }
                _uiState.update { it.copy(isAddTransactionVisible = false) }
                loadStartData() // Reload to show new data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
