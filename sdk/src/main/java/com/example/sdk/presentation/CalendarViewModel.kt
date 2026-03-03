package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdk.data.network.dto.RecurrenceFrequency
import com.example.sdk.domain.model.DayData
import com.example.sdk.domain.model.Transaction
import com.example.sdk.domain.repository.TransactionsRepository
import com.example.sdk.presentation.models.ViewModeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.asStateFlow
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

    private var domainTransactions: List<Transaction> = emptyList()

    private fun loadStartData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repo.getAll()
                }
                domainTransactions = data.mapNotNull { it.toDomain() }
            } catch (e: Exception) {
                e.printStackTrace()
                domainTransactions = emptyList()
            }

            rebuildMonth()
        }
    }

    private fun rebuildMonth() {
        val month = uiState.value.selectedPeriod
        val days = buildCalendar(domainTransactions, month)

        _uiState.update {
            it.copy(daysData = days)
        }
    }

    private fun buildCalendar(
        transactions: List<Transaction>,
        monthCalendar: Calendar
    ): Map<Int, DayData> {
        val year = monthCalendar.get(Calendar.YEAR)
        val month = monthCalendar.get(Calendar.MONTH)

        val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val calendarMap = (1..daysInMonth).associateWith {
            DayData(day = it, transactions = emptyList(), hasRecurring = false)
        }.toMutableMap()

        val now = Calendar.getInstance()

        transactions.forEach { tx ->
            val txCal = tx.date.clone() as Calendar

            if (txCal.get(Calendar.YEAR) == year && txCal.get(Calendar.MONTH) == month) {
                val day = txCal.get(Calendar.DAY_OF_MONTH)
                val old = calendarMap[day]!!
                calendarMap[day] = old.copy(
                    transactions = old.transactions + tx,
                    hasRecurring = old.hasRecurring || (tx.recurrenceRule != null)
                )
            }

            tx.recurrenceRule?.let { rule ->
                val recurCal = txCal.clone() as Calendar
                val until =
                    rule.untilDate ?: Calendar.getInstance().apply { add(Calendar.YEAR, 10) }
                recurCal.addByFrequency(rule.frequency, rule.interval)

                while (!recurCal.after(until)) {
                    if (recurCal.get(Calendar.YEAR) == year && recurCal.get(Calendar.MONTH) == month) {
                        val day = recurCal.get(Calendar.DAY_OF_MONTH)
                        val old = calendarMap[day]!!
                        calendarMap[day] = old.copy(
                            transactions = old.transactions + tx,
                            hasRecurring = true
                        )
                    }

                    recurCal.addByFrequency(rule.frequency, rule.interval)
                }
            }
        }

        return calendarMap
    }

    private fun Calendar.addByFrequency(
        frequency: RecurrenceFrequency,
        interval: Int
    ) {
        when (frequency) {
            RecurrenceFrequency.DAILY -> add(Calendar.DAY_OF_MONTH, interval)
            RecurrenceFrequency.WEEKLY -> add(Calendar.WEEK_OF_YEAR, interval)
            RecurrenceFrequency.MONTHLY -> add(Calendar.MONTH, interval)
            RecurrenceFrequency.YEARLY -> add(Calendar.YEAR, interval)
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

            CalendarUiAction.OnNextPeriodClick-> {
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
                viewModelScope.launch {
                    _sideEffect.emit(CalendarSideEffect.OpenAddScreen)
                }
            }
        }
    }
}