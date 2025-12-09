package com.example.sdk.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sdk.data.network.dto.TransactionDto
import com.example.sdk.data.network.dto.TransactionType
import com.example.sdk.domain.repository.TransactionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repo: TransactionsRepository
) : ViewModel() {

    var transactions by mutableStateOf<List<TransactionDto>>(emptyList())
        private set

    var amountInput by mutableStateOf("")
    var categoryInput by mutableStateOf("")
    var noteInput by mutableStateOf("")

    init {
        reload()
    }

    private fun reload() {
        viewModelScope.launch {
            transactions = repo.getAll()
        }
    }

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
            reload()
            amountInput = ""
            categoryInput = ""
            noteInput = ""
        }
    }
}