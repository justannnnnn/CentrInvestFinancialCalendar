package com.example.sdk.data.repository

import com.example.sdk.data.network.CalendarApi
import com.example.sdk.data.network.PartialCalendarOperation
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
        val response = api.createOperation(operation)

        if (!response.isSuccessful) {
            error("Не удалось добавить операцию: ${response.code()}")
        }
    }

    override suspend fun updateOperation(operation: CalendarOperation) {
        val operationId = operation.id
            ?: error("Нельзя обновить операцию без id")

        val response = api.updateOperation(
            id = "eq.$operationId",
            operation = PartialCalendarOperation(
                title = operation.title,
                amount = operation.amount,
                dateTime = operation.dateTime,
                categoryId = operation.categoryId,
                isCustom = operation.isCustom,
                status = operation.status,
                recurrence = operation.recurrence
            )
        )

        if (!response.isSuccessful) {
            error("Не удалось обновить операцию: ${response.code()}")
        }
    }

    override suspend fun deleteOperation(operationId: Int) {
        val response = api.deleteOperation(
            id = "eq.$operationId"
        )

        if (!response.isSuccessful) {
            error("Не удалось удалить операцию: ${response.code()}")
        }
    }
}