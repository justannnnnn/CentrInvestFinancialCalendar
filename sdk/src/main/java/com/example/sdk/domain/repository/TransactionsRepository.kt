package com.example.sdk.domain.repository

import com.example.sdk.data.network.dto.CalendarOperation
import com.example.sdk.data.network.dto.FinancialCalendarInput

interface TransactionsRepository {
    suspend fun getCalendarData(): FinancialCalendarInput

    suspend fun addOperation(operation: CalendarOperation)

    suspend fun updateOperation(operation: CalendarOperation)

    suspend fun deleteOperation(operationId: Int)
}