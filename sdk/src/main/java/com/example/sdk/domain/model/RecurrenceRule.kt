package com.example.sdk.domain.model

import com.example.sdk.data.network.dto.RecurrenceFrequency
import java.util.Calendar

data class RecurrenceRule(
    val frequency: RecurrenceFrequency,
    val interval: Int,
    val untilDate: Calendar? = null
)