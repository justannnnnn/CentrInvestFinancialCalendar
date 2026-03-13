package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdk.data.network.dto.RecurrenceFrequency
import com.example.sdk.data.network.dto.TransactionDto
import com.example.sdk.data.network.dto.TransactionType
import com.example.sdk.domain.model.DayData
import com.example.sdk.domain.model.Transaction
import com.example.sdk.domain.repository.TransactionsRepository
import com.example.sdk.presentation.models.ViewModeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID
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

    fun loadStartData() {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    repo.getAll()
                }
                domainTransactions = data.map { it.toDomain() }
            } catch (e: Exception) {
                e.printStackTrace()
                domainTransactions = emptyList()
            }

            rebuildMonth()
        }
    }

    private fun rebuildMonth() {
        val month = uiState.value.selectedMonth
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
                val until = rule.untilDate ?: Calendar.getInstance().apply { add(Calendar.YEAR, 10) }
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

            CalendarUiAction.OnPrevMonthClick -> {
                val newMonth = (_uiState.value.selectedMonth.clone() as Calendar).apply {
                    add(Calendar.MONTH, -1)
                }
                _uiState.update { it.copy(selectedMonth = newMonth) }
                rebuildMonth()
            }

            CalendarUiAction.OnNextMonthClick -> {
                val newMonth = (_uiState.value.selectedMonth.clone() as Calendar).apply {
                    add(Calendar.MONTH, 1)
                }
                _uiState.update { it.copy(selectedMonth = newMonth) }
                rebuildMonth()
            }

            is CalendarUiAction.OnDaySelected -> {
                _uiState.update {
                    it.copy(
                        selectedDate = LocalDate.of(
                            it.selectedMonth.get(Calendar.YEAR),
                            it.selectedMonth.get(Calendar.MONTH) + 1,
                            action.day
                        ),
                        showBottomSheet = true
                    )
                }
            }

            is CalendarUiAction.OnViewModeSelected -> {
                _uiState.update { it.copy(selectedViewMode = action.mode) }
            }

            CalendarUiAction.OnDismissBottomSheet -> {
                _uiState.update { it.copy(showBottomSheet = false) }
            }

            CalendarUiAction.OnAddClick -> {
                viewModelScope.launch {
                    _sideEffect.emit(CalendarSideEffect.OpenAddScreen)
                }
            }
        }
    }

    // для получения операций за день
    fun getTransactionsForDay(day: Int, month: Calendar, transactions: List<Transaction>): List<Transaction> {
        val year = month.get(Calendar.YEAR)
        val monthValue = month.get(Calendar.MONTH)

        return transactions.filter { tx ->
            val txCal = tx.date.clone() as Calendar
            txCal.get(Calendar.YEAR) == year &&
                    txCal.get(Calendar.MONTH) == monthValue &&
                    txCal.get(Calendar.DAY_OF_MONTH) == day
        }
    }
}