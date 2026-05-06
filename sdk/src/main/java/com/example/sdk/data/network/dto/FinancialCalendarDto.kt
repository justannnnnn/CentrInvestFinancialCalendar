package com.example.sdk.data.network.dto

import com.google.gson.annotations.SerializedName

data class FinancialCalendarInput(
    val categories: List<CalendarCategory>,
    val operations: List<CalendarOperation>
)

data class CalendarCategory(
    val id: Int,
    val name: String,        // "Покупки"
    val iconUrl: String,     // "Url"
    val color: String,       // "#FFFFFF"
    val isIncome: Boolean,   // доход или расход
    val amount: Double      // сумма доходов по категории
)

data class CalendarOperation(
    val id: Int,
    val title: String,               // "Покупка кофе"
    val amount: Long,                // Signed в копейках
    val dateTime: Long,              // Unix Timestamp в UTC
    val categoryId: Int,             // ссылка на CalendarCategory.id
    val isCustom: Boolean,           // true = вымышленная/создана юзером, false = реальная банковская
    val status: OperationStatus?,    // null = будущая(можем определить по времени) - planned, SUCCESS/ERROR = прошлое или настоящее
    val recurrence: Recurrence?      // null = не регулярная
)

enum class OperationStatus {
    @SerializedName("SUCCESS") SUCCESS,
    @SerializedName("ERROR") ERROR
}

data class Recurrence(
    val every: Int,              // 30
    val unit: RecurrenceUnit,    // DAY / WEEK / MONTH
    val time: String             // "12:00"
)

enum class RecurrenceUnit {
    @SerializedName("DAY") DAY,
    @SerializedName("WEEK") WEEK,
    @SerializedName("MONTH") MONTH
}
