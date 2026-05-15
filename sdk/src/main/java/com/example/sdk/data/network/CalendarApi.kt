package com.example.sdk.data.network

import com.example.sdk.data.network.dto.CalendarCategory
import com.example.sdk.data.network.dto.CalendarOperation
import com.example.sdk.data.network.dto.FinancialCalendarInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.DELETE

interface CalendarApi {
    @GET("rpc/calendar_data")
    suspend fun getFinancialCalendar(): FinancialCalendarInput

    @GET("categories?select=*")
    suspend fun getCategories(): List<CalendarCategory>

    @GET("operations?select=*")
    suspend fun getOperations(): List<CalendarOperation>

    @POST("operations")
    suspend fun createOperation(@Body operation: CalendarOperation): Response<Unit>

    @PATCH("operations")
    suspend fun updateOperation(
        @Query("id") id: String,
        @Body operation: PartialCalendarOperation
    ): Response<Unit>

    @POST("categories")
    suspend fun createCategory(@Body category: CalendarCategory): Response<Unit>

    @DELETE("operations")
    suspend fun deleteOperation(
        @Query("id") id: String
    ): Response<Unit>
}

/**
 * Вспомогательный класс для частичного обновления операции
 */
data class PartialCalendarOperation(
    val title: String? = null,
    val amount: Long? = null,
    val dateTime: Long? = null,
    val categoryId: Int? = null,
    val isCustom: Boolean? = null,
    val status: Int? = null,
    val recurrence: com.example.sdk.data.network.dto.Recurrence? = null
)
