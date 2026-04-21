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
import com.example.sdk.domain.model.Category
import com.example.sdk.ui.theme.CalendarTheme

@SuppressLint("DefaultLocale")
@Composable
fun StatsCategoryList(categoriesToSum: Map<Category, Long>) {
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
            .sortedByDescending { it.value }
            .forEach { (category, sum) ->
                val percentage = if (category.isIncome || totalExpenses == 0L) {
                    0f
                } else {
                    (sum.toFloat() / totalExpenses.toFloat() * 100f)
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
                                    color = Color(category.color).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.icon,
                                style = typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = category.title,
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
                        text = "${if (!category.isIncome) "- " else ""}${sum.formatSum()} ₽",
                        style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = if (category.isIncome) colors.primary else colors.textPrimary
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