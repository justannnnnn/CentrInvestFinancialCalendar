package com.example.sdk.data.network.dto

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    val id: String? = null,
    val amount: Long,
    val type: TransactionType,
    val category: String,
    @SerializedName("date_epoch_ms")
    val dateEpochMs: Long,
    val note: String? = null,
    @SerializedName("is_planned")
    val isPlanned: Boolean = false,
    @SerializedName("recurrence_json")
    val recurrenceRule: RecurrenceRuleDto? = null
)

enum class TransactionType {
    INCOME,
    EXPENSE
}