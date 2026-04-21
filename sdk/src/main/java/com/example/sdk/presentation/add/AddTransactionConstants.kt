package com.example.sdk.presentation.add

import com.example.sdk.domain.model.Category

data class AddCategoryUi(
    val category: Category,
    val subtitle: String? = null
)

object AddTransactionConstants {
    val categories: List<AddCategoryUi> = listOf(
        AddCategoryUi(Category.SHOPPING),
        AddCategoryUi(Category.HOUSE),
        AddCategoryUi(Category.GROCERIES),
        AddCategoryUi(Category.TRANSPORT),
        AddCategoryUi(Category.ENTERTAINMENT),
        AddCategoryUi(Category.WORK),
        AddCategoryUi(Category.HEALTH)
    )

    val recurringUnits: List<String> = listOf(
        "дней",
        "недель",
        "месяцев"
    )
}