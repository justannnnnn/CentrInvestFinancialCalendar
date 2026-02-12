package com.example.sdk.data.network.dto

import com.example.sdk.domain.model.RecurrenceRule
import java.util.Calendar

data class RecurrenceRuleDto(
    val frequency: RecurrenceFrequency,
    val interval: Int = 1,
    val untilEpochMs: Long? = null
) {
    fun toDomain(): RecurrenceRule {
        val cal = untilEpochMs?.let {
            Calendar.getInstance().apply { timeInMillis = it }
        }

        return RecurrenceRule(
            frequency = frequency,
            interval = interval,
            untilDate = cal
        )
    }
}

enum class RecurrenceFrequency {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}