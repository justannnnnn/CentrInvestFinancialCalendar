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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.presentation.utils.AutoSizeText
import com.example.sdk.ui.theme.CalendarTheme
import kotlin.math.abs

@Composable
fun DonutChartWithStats(
    categoriesToSum: Map<CalendarCategoryUi, Long>,
    centerText: String,
    amountColor: Color
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    val totalAmount = categoriesToSum.values.sumOf { abs(it) }

    val segments = mutableListOf<DonutSegment>()
    var startAngle = -90f

    if (totalAmount > 0) {
        categoriesToSum.forEach { (category, sum) ->

            val sweepAngle =
                (sum.toFloat() / totalAmount.toFloat()) * 360f

            val parsedColor = try {
                Color(android.graphics.Color.parseColor(category.color))
            } catch (e: Exception) {
                colors.textSecondary
            }

            segments.add(
                DonutSegment(
                    color = parsedColor,
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
            .size(160.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(120.dp)
        ) {
            segments.forEach { segment ->

                drawArc(
                    color = segment.color,
                    startAngle = segment.startAngle,
                    sweepAngle = segment.sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = 20.dp.toPx(),
                        cap = StrokeCap.Butt
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
                    text = centerText,
                    style = typography.labelSmall,
                    color = colors.textSecondary
                )

                AutoSizeText(
                    text = "${totalAmount.formatSum()} ₽",
                    maxTextSize = 18.sp,
                    minTextSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    color = amountColor
                )
            }
        }
    }
}
