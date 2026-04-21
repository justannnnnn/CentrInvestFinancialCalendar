package com.example.sdk.presentation.statistics

import java.text.NumberFormat
import java.util.Locale

fun Long.formatSum(): String {
    return NumberFormat
        .getNumberInstance(Locale("ru", "RU"))
        .format(this)
}