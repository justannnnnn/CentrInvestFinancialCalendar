package com.example.sdk.domain.repository

import com.example.sdk.data.network.dto.TransactionDto

interface TransactionsRepository {
    suspend fun getAll(): List<TransactionDto>
    suspend fun add(transaction: TransactionDto)
}