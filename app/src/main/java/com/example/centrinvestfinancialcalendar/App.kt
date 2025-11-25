package com.example.centrinvestfinancialcalendar

import android.app.Application
import com.example.sdk.sdk.CalendarConfig
import com.example.sdk.sdk.CalendarSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        CalendarSdk.init(
            context = this,
            config = CalendarConfig(
                supabaseAnonKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpxaGtiamtyY2Jwemxub293aHJyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIzMjk5NjUsImV4cCI6MjA3NzkwNTk2NX0.Lk07zJr1i0TQ53FGX24nhrsXzqNM3YyvJj0Ntqj3rW0",
                baseUrl = "https://jqhkbjkrcbpzlnoowhrr.supabase.co/rest/v1/"
            )
        )
    }
}