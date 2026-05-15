package com.example.sdk.domain.model

import com.example.sdk.data.network.dto.CalendarOperation
import com.example.sdk.data.network.dto.OperationStatus
import com.example.sdk.data.network.dto.Recurrence

data class FinancialCalendarInput(
    val categories: List<CalendarCategoryUi>,
    val operations: List<CalendarOperationUi>
)

data class CalendarCategoryUi(
    val id: Int,
    val name: String,        // "Покупки"
    val iconUrl: String,     // "Url"
    val color: String,       // "#FFFFFF"
    val isIncome: Boolean,   // доход или расход
    val amount: Double      // сумма доходов по категории
)

data class CalendarOperationUi(
    val id: Int,
    val title: String,
    val amount: Double,
    val dateTime: Long,
    val categoryId: Int,
    val isCustom: Boolean,
    val status: OperationStatus?,
    val recurrence: Recurrence?
) {
    fun toDto() = CalendarOperation(
        id = id.takeIf { it > 0 },
        title = title,
        amount = (amount * 100).toLong(),
        dateTime = dateTime,
        categoryId = categoryId,
        isCustom = isCustom,
        status = when (status) {
            OperationStatus.SUCCESS -> 0
            OperationStatus.ERROR -> 1
            null -> null
        },
        recurrence = recurrence
    )
}

data class DayData(
    val day: Int,
    val operations: List<CalendarOperationUi>,
    val hasRecurring: Boolean
)
