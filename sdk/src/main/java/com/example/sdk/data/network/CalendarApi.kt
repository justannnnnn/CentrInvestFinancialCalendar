package com.example.sdk.data.network

import com.example.sdk.data.network.dto.TransactionDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CalendarApi {
    @GET("transactions?select=*")
    suspend fun getAllTransactions(): List<TransactionDto>

    @POST("transactions")
    suspend fun createTransaction(@Body tx: TransactionDto): Response<Unit>
}