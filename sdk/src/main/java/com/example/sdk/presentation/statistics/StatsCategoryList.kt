package com.example.sdk.presentation.statistics

import android.annotation.SuppressLint
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.ui.theme.CalendarTheme
import kotlin.math.abs

@SuppressLint("DefaultLocale")
@Composable
fun StatsCategoryList(categoriesToSum: Map<CalendarCategoryUi, Long>) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val totalExpenses = categoriesToSum.filter { it.key.isIncome.not() }.values.sum()

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        categoriesToSum.entries
            .sortedByDescending { abs(it.value) }
            .forEach { (category, sum) ->
                val percentage = if (category.isIncome || totalExpenses == 0L) {
                    0f
                } else {
                    (sum.toFloat() / totalExpenses.toFloat() * 100f)
                }

                val parsedColor = try {
                    Color(android.graphics.Color.parseColor(category.color))
                } catch (e: Exception) {
                    colors.textSecondary
                }

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
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = parsedColor.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = category.iconUrl,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = category.name,
                                style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = colors.textPrimary
                            )
                            if (category.isIncome.not()) {
                                Text(
                                    text = String.format("%.1f%%", percentage),
                                    style = typography.labelSmall,
                                    color = colors.textSecondary
                                )
                            }
                        }
                    }

                    Text(
                        text = "${sum.formatSum()} ₽",
                        style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = if (category.isIncome) colors.income else colors.expense
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.borderLight)
                )
            }
    }
}
