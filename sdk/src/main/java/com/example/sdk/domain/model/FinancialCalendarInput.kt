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
    val title: String,               // "Покупка кофе"
    val amount: Double,                // Signed в копейках
    val dateTime: Long,              // Unix Timestamp в UTC
    val categoryId: Int,             // ссылка на CalendarCategory.id
    val isCustom: Boolean,           // true = вымышленная/создана юзером, false = реальная банковская
    val status: OperationStatus?,    // null = будущая(можем определить по времени) - planned, SUCCESS/ERROR = прошлое или настоящее
    val recurrence: Recurrence?      // null = не регулярная
) {
    fun toDto() = CalendarOperation(
        id = id,
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
