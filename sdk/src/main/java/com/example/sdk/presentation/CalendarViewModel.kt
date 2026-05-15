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

                domainCategories = data.categories.map { it.toUi() }
                domainOperations = data.operations.map { it.toUi() }

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
