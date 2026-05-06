package com.example.sdk.sdk

import com.example.sdk.data.network.dto.CalendarOperation

interface CalendarEventListener {
    fun onTransactionAdded(operation: CalendarOperation)
    fun onTransactionEdited(operation: CalendarOperation)
    fun onTransactionDeleted(id: Int)
    fun onStatisticsViewed()
}
