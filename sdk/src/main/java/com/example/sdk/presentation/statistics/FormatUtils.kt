package com.example.sdk.presentation.statistics

import java.text.NumberFormat
import java.util.Locale

fun Long.formatSum(): String {
    return NumberFormat
        .getNumberInstance(Locale("ru", "RU"))
        .format(this)
}

fun Double.formatSum(): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru", "RU"))
    formatter.minimumFractionDigits = 0
    formatter.maximumFractionDigits = 2
    return formatter.format(this)
}
