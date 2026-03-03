package com.example.sdk.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))

fun getDateFormat(date: Date) = dateFormat.format(date).replaceFirstChar { it.uppercase() }