package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.selects.select
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TransactionsRepository
) : ViewModel() {

    var transactions by mutableStateOf<List<TransactionDto>>(emptyList())
        private set

    var amountInput by mutableStateOf("")
    var categoryInput by mutableStateOf("")
    var noteInput by mutableStateOf("")

    fun addTransaction() {
        val tx = TransactionDto(
            id = UUID.randomUUID().toString(),
            amount = amountInput.toLongOrNull()?.times(100) ?: 0,
            type = TransactionType.EXPENSE,
            category = categoryInput,
            dateEpochMs = System.currentTimeMillis(),
            note = noteInput,
            isPlanned = false
        )

        viewModelScope.launch {
            repo.add(tx)
            amountInput = ""
            categoryInput = ""
            noteInput = ""
        }
    }

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

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
}