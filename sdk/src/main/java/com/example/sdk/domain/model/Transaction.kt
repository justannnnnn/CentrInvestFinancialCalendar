package com.example.sdk.domain.model

import com.example.sdk.data.network.dto.TransactionType
import java.util.Calendar

data class Transaction(
    val id: String,
    val amount: Long,
    val type: TransactionType,
    val category: Category,
    val date: Calendar,
    val note: String?,
    val isPlanned: Boolean,
    val recurrenceRule: RecurrenceRule?
)

data class DayData(
    val day: Int,
    val transactions: List<Transaction>,
    val hasRecurring: Boolean
)