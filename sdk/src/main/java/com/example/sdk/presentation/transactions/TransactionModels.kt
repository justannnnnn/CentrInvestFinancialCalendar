package com.example.sdk.presentation.transactions

data class TransactionItem(
    val name: String,
    val date: String,
    val amount: Int,
    val icon: String,
    val color: Long
)