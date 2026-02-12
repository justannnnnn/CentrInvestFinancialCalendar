package com.example.sdk.data.network.dto

import com.example.sdk.domain.model.Transaction
import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.UUID

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
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = id ?: UUID.randomUUID().toString(),
            amount = amount,
            type = type,
            category = category,
            date = Calendar.getInstance().apply { timeInMillis = dateEpochMs },
            note = note,
            isPlanned = isPlanned,
            recurrenceRule = recurrenceRule?.toDomain()
        )
    }
}

enum class TransactionType {
    INCOME,
    EXPENSE
}