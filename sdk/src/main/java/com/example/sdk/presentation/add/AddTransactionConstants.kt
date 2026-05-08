package com.example.sdk.presentation.add

import com.example.sdk.domain.model.CalendarCategoryUi

data class AddCategoryUi(
    val category: CalendarCategoryUi,
    val subtitle: String? = null
)

object AddTransactionConstants {
    // This list should ideally be dynamic from the repository.
    // Keeping it empty or as a placeholder if needed, but it's better to use UI state.
    val recurringUnits: List<String> = listOf(
        "дней",
        "недель",
        "месяцев"
    )
}
