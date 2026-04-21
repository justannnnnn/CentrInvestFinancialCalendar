package com.example.sdk.presentation.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.domain.model.Category
import com.example.sdk.presentation.utils.AutoSizeText
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun DonutChartWithStats(categoriesToSum: Map<Category, Long>) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val expenseCategories = categoriesToSum.filter { it.key.isIncome.not() }

    val totalExpenses = expenseCategories.values.sumOf { it }
    val totalIncome = categoriesToSum.filter { it.key.isIncome }.values.sumOf { it }
    val balance = totalIncome - totalExpenses

    val segments = mutableListOf<DonutSegment>()
    var startAngle = -90f

    if (totalExpenses > 0) {
        expenseCategories.forEach { (category, sum) ->
            val sweepAngle = (sum.toFloat() / totalExpenses.toFloat()) * 360f
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
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
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

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = colors.surface,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.balance),
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )
                AutoSizeText(
                    text = "${balance.formatSum()} ₽",
                    maxTextSize = 18.sp,
                    minTextSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    color = if (balance < 0) colors.expense else colors.primary
                )
            }
        }
    }
}