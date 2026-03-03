package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdk.data.network.dto.RecurrenceFrequency
import com.example.sdk.data.network.dto.RecurrenceRuleDto
import com.example.sdk.data.network.dto.TransactionDto
import com.example.sdk.data.network.dto.TransactionType
import com.example.sdk.domain.repository.TransactionsRepository
import com.example.sdk.presentation.models.ViewModeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TransactionsRepository
) : ViewModel() {

    var transactions by mutableStateOf<List<TransactionDto>>(emptyList())
        private set

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    fun refreshTransactions() {
        viewModelScope.launch {
            // transactions = repo.getAll()
        }
    }

    /**
     * amountMinorSigned — копейки со знаком:
     *  +12345 = доход 123.45
     *  -12345 = расход 123.45
     */
    fun addTransaction(
        amountMinorSigned: Long,
        category: String,
        note: String,
        date: LocalDate,
        isRecurring: Boolean,
        recurringFrequency: RecurrenceFrequency = RecurrenceFrequency.MONTHLY,
        recurringInterval: Int = 1,
        recurringUntilEpochMs: Long? = null
    ) {
        val epochMs = date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val rule: RecurrenceRuleDto? =
            if (isRecurring) RecurrenceRuleDto(
                frequency = recurringFrequency,
                interval = recurringInterval.coerceAtLeast(1),
                untilEpochMs = recurringUntilEpochMs
            ) else null

        val tx = TransactionDto(
            id = UUID.randomUUID().toString(),
            amount = abs(amountMinorSigned), // копейки без знака
            type = if (amountMinorSigned >= 0) TransactionType.INCOME else TransactionType.EXPENSE,
            category = category,
            dateEpochMs = epochMs,
            note = note,
            isPlanned = isRecurring,
            recurrenceRule = rule
        )

        viewModelScope.launch {
            repo.add(tx)
            // refreshTransactions()
        }
    }

    fun onPrevMonth() {
        _uiState.update { it.copy(currentYearMonth = it.currentYearMonth.minusMonths(1)) }
    }

    fun onNextMonth() {
        _uiState.update { it.copy(currentYearMonth = it.currentYearMonth.plusMonths(1)) }
    }

    fun onSelectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onChangeViewMode(mode: ViewModeTab) {
        _uiState.update { it.copy(selectedViewMode = mode) }
    }

    fun onChangePeriod(start: LocalDate, end: LocalDate) {
        val (s, e) = if (start.isAfter(end)) end to start else start to end
        _uiState.update { it.copy(periodStart = s, periodEnd = e) }
    }
}