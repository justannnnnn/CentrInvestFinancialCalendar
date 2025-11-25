package com.example.sdk.data.network

import com.example.sdk.sdk.CalendarSdk
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://jqhkbjkrcbpzlnoowhrr.supabase.co/rest/v1/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(SupabaseAuthInterceptor(CalendarSdk.SUPABASE_ANON_KEY))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val api: CalendarApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(CalendarApi::class.java)
}
