package com.example.sdk.presentation.statistics

import androidx.compose.ui.graphics.Color
import com.example.sdk.domain.model.Category

data class CategoryItem(
    val icon: String,
    val name: String,
    val amount: Int,
    val isIncome: Boolean = false,
    val color: Long
)

data class DonutSegment(
    val color: Color,
    val startAngle: Float,
    val sweepAngle: Float,
    val category: Category
)