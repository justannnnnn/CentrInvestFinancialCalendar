package com.example.sdk.sdk

data class CalendarConfig(
    // TODO val theme: ThemeConfig? = null, // цвета, шрифты — позже
    val supabaseAnonKey: String,
    val baseUrl: String? = null, // для API
    val enableAnalytics: Boolean = true
)