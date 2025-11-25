package com.example.sdk.data.repository

import com.example.sdk.data.network.CalendarApi
import com.example.sdk.data.network.dto.TransactionDto
import com.example.sdk.domain.repository.TransactionsRepository
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val api: CalendarApi
) : TransactionsRepository {
    override suspend fun getAll(): List<TransactionDto> {
        return api.getAllTransactions()
    }

    override suspend fun add(transaction: TransactionDto) {
        api.createTransaction(transaction)
    }
}