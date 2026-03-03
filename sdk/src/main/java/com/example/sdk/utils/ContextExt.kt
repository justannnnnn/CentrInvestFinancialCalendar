package com.example.sdk.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.getQuantityStringRu(id: Int, quantity: Int): String {
    return getQuantityStringRu(id, quantity, quantity)
}

private fun Context.getQuantityStringRu(id: Int, quantity: Int, vararg formatArgs: Any): String {
    return getContextRu().resources.getQuantityString(id, quantity, *formatArgs)
}

private fun Context.getContextRu(): Context {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale("ru", "RU"))
    return createConfigurationContext(configuration)
}