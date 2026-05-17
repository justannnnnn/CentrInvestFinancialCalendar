package com.example.sdk.data.network.dto

import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

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
    val amount: Long      // сумма доходов по категории
) {
    fun toUi() = CalendarCategoryUi(
        id = id,
        name = name,
        iconUrl = iconUrl,
        color = color,
        isIncome = isIncome,
        amount = (amount / 100f).toDouble()
    )
}

data class CalendarOperation(
    val id: Int? = null,
    val title: String,               // "Покупка кофе"
    val amount: Long,
    @SerialName("date_time")  // Signed в копейках
    val dateTime: Long,              // Unix Timestamp в UTC
    val categoryId: Int,             // ссылка на CalendarCategory.id
    val isCustom: Boolean,           // true = вымышленная/создана юзером, false = реальная банковская
    val status: Int?,    // null = будущая(можем определить по времени) - planned, SUCCESS/ERROR = прошлое или настоящее
    val recurrence: Recurrence?      // null = не регулярная
) {
    fun toUi() = CalendarOperationUi(
        id = id ?: 0,
        title = title,
        amount = (amount / 100f).toDouble(),
        dateTime = dateTime,
        categoryId = categoryId,
        isCustom = isCustom,
        status = when (status) {
            0 -> OperationStatus.SUCCESS
            1 -> OperationStatus.ERROR
            else -> null
        },
        recurrence = recurrence
    )
}

enum class OperationStatus {
    @SerializedName("SUCCESS")
    SUCCESS,
    @SerializedName("ERROR")
    ERROR
}

data class Recurrence(
    val every: Int,              // 30
    val unit: RecurrenceUnit,    // DAY / WEEK / MONTH
    val time: String             // "12:00"
)

enum class RecurrenceUnit {
    @SerializedName("DAY")
    DAY,
    @SerializedName("WEEK")
    WEEK,
    @SerializedName("MONTH")
    MONTH
}
