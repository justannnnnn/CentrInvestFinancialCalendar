package com.example.sdk.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun AddTransactionPreviewCard(
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val icons = CalendarTheme.icons

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        colors.addSheetCategoryChip,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛒",
                    style = typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Покупки",
                    style = typography.bodyLarge,
                    color = colors.textPrimary
                )

                Text(
                    text = "Категория операции",
                    style = typography.bodySmall,
                    color = colors.textSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        colors.addSheetRecurringAccent,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconWrapper(
                    iconRes = icons.recurring,
                    color = colors.addSheetAccent,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .background(colors.surface, RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = colors.borderLight,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "1 250 ₽",
                    style = typography.bodyLarge,
                    color = colors.textPrimary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 42.dp, height = 42.dp)
                    .background(colors.addSheetAccent, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = typography.titleMedium,
                    color = colors.surface
                )
            }
        }
    }
}