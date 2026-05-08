package com.example.sdk.presentation.add

import com.example.sdk.domain.model.CalendarCategoryUi

data class AddTransactionUiState(
    val selectedCategory: CalendarCategoryUi? = null,
    val isCategoryExpanded: Boolean = false,
    val description: String = "",
    val amount: String = "0",
    val isIncome: Boolean = false,
    val isRecurring: Boolean = false,
    val recurringValue: String = "30",
    val recurringUnitIndex: Int = 0,
    val recurringTime: String = "12:00"
)
