package com.example.sdk.data.repository

import com.example.sdk.data.network.CalendarApi
import com.example.sdk.data.network.dto.CalendarOperation
import com.example.sdk.data.network.dto.FinancialCalendarInput
import com.example.sdk.domain.repository.TransactionsRepository
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val api: CalendarApi
) : TransactionsRepository {
    override suspend fun getCalendarData(): FinancialCalendarInput {
        return api.getFinancialCalendar()
    }

    override suspend fun addOperation(operation: CalendarOperation) {
        api.createOperation(operation)
    }
}
