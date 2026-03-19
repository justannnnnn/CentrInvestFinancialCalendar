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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.domain.model.Category
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary

@SuppressLint("DefaultLocale")
@Composable
fun StatsCategoryList(categoriesToSum: Map<Category, Long>) {
    val totalExpenses = categoriesToSum.filter { it.key.isIncome.not() }.values.sum()

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        categoriesToSum.entries.sortedByDescending { it.value }
            .forEach { (category, sum) ->
                val percentage = if (category.isIncome) {
                    0f
                } else {
                    (sum.toFloat() / totalExpenses.toFloat() * 100)
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

                        Column {
                            Text(
                                text = category.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray900
                            )
                            if (category.isIncome.not()) {
                                Text(
                                    text = String.format("%.1f%%", percentage),
                                    fontSize = 12.sp,
                                    color = Gray500
                                )
                            }
                        }
                    }

                    Text(
                        text = "${if (!category.isIncome) "- " else ""}${sum.formatSum()} ₽",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (category.isIncome) GreenPrimary else Gray900
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray500.copy(alpha = 0.2f))
                )
            }
    }
}