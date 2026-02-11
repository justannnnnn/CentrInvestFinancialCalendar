package com.example.sdk.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun StatsCategoryList() {
    val categories = listOf(
        CategoryItem("💼", "Работа", 80_000, true, 0xFF00A86B),
        CategoryItem("🏠", "Дом и быт", 15_000, false, 0xFF32CD32),
        CategoryItem("🛒", "Продукты", 4_650, false, 0xFFFFA500),
        CategoryItem("💳", "Покупки", 2_570, false, 0xFFF95E5A),
        CategoryItem("🚕", "Транспорт", 900, false, 0xFF1E90FF),
        CategoryItem("🏥", "Здоровье", 850, false, 0xFF8A2BE2),
        CategoryItem("🎬", "Развлечения", 600, false, 0xFFFFB800)
    )

    val totalExpenses = categories.filter { !it.isIncome }.sumOf { it.amount }.toFloat()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        categories.forEach { category ->
            val percentage = if (category.isIncome) 0f else
                (category.amount / totalExpenses * 100).toInt().toFloat()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Иконка
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(category.color).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.icon,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Название и сумма
                    Column {
                        Text(
                            text = category.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray900
                        )
                        if (!category.isIncome) {
                            Text(
                                text = "${percentage.toInt()}%",
                                fontSize = 12.sp,
                                color = Gray500
                            )
                        }
                    }
                }

                // Сумма
                Text(
                    text = "${if (!category.isIncome) "- " else ""}${category.amount} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (category.isIncome) GreenPrimary else Gray900
                )
            }

            if (categories.last() != category) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray500.copy(alpha = 0.2f))
                )
            }
        }
    }
}