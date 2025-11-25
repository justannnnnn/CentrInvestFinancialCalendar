package com.example.sdk.data.network.dto

data class RecurrenceRuleDto(
    val frequency: RecurrenceFrequency,
    val interval: Int = 1,
    val untilEpochMs: Long? = null
)

enum class RecurrenceFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}