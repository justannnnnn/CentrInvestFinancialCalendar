package com.example.sdk.sdk

import android.content.Context

object CalendarSdk {
    lateinit var SUPABASE_ANON_KEY: String

    fun init(context: Context, config: CalendarConfig) {
        SUPABASE_ANON_KEY = config.supabaseAnonKey
    }

    // TODO пустое тело
    fun setListener(listener: CalendarEventListener) {}
    fun openDemo(context: Context) {}
}