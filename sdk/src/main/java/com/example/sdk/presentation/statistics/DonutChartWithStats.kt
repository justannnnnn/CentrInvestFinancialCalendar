package com.example.sdk.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import kotlin.math.abs

@Composable
fun DonutChartWithStats() {
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
    val totalIncome = categories.filter { it.isIncome }.sumOf { it.amount }
    val balance = totalIncome - totalExpenses.toInt()

    // Подготавливаем данные для диаграммы (только расходы)
    val expenseCategories = categories.filter { !it.isIncome }
    val total = expenseCategories.sumOf { it.amount }.toFloat()

    // Создаем список сегментов с начальными углами
    val segments = mutableListOf<DonutSegment>()
    var startAngle = -90f // Начинаем с верхней точки (12 часов)

    expenseCategories.forEach { category ->
        val sweepAngle = (category.amount / total) * 360f
        segments.add(
            DonutSegment(
                color = Color(category.color),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                category = category
            )
        )
        startAngle += sweepAngle
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Donut Chart
        Canvas(modifier = Modifier.size(180.dp)) {
            segments.forEach { segment ->
                drawArc(
                    color = segment.color,
                    startAngle = segment.startAngle,
                    sweepAngle = segment.sweepAngle,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 20.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Butt
                    ),
                    size = size
                )
            }
        }

        // Внутренний круг с итогом
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (balance < 0) "Расходы" else "Доходы",
                    fontSize = 12.sp,
                    color = Gray500
                )
                Text(
                    text = "${if (balance < 0) "- " else ""}${abs(balance)} ₽",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (balance < 0) Color(0xFFF95E5A) else GreenPrimary
                )
            }
        }
    }
}