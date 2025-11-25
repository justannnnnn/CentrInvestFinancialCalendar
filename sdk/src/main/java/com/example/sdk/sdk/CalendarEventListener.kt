package com.example.sdk.sdk

import com.example.sdk.data.network.dto.TransactionDto

interface CalendarEventListener {
    fun onTransactionAdded(tx: TransactionDto)
    fun onTransactionEdited(tx: TransactionDto)
    fun onTransactionDeleted(id: String)
    fun onStatisticsViewed()// TODO DateRange
}