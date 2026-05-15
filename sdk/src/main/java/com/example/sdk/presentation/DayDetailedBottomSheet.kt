package com.example.sdk.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.domain.model.CalendarOperationUi
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.utils.getDateFormat
import com.example.sdk.utils.getQuantityStringRu
import java.util.Calendar

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DayDetailedBottomSheet(
    dayOperations: List<CalendarOperationUi>,
    categories: List<CalendarCategoryUi>,
    selectedDay: Calendar,
    onClickAdd: () -> Unit,
    onClickTransaction: (CalendarOperationUi) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7).dp)
            .verticalScroll(scrollState)
            .background(colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlusButton(onClick = onClickAdd)
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = getDateFormat(selectedDay.time),
            style = typography.titleLarge,
            color = colors.textPrimary
        )

        DayStats(
            operationCount = dayOperations.size,
            dayBalance = dayOperations.sumOf { op -> op.amount }
        )

        if (dayOperations.isEmpty()) {
            EmptyBottomSheetState()
        } else {
            dayOperations.forEach { operation ->
                val category = categories.find { it.id == operation.category?.id }
                TransactionItem(
                    icon = category?.iconUrl ?: "❓",
                    name = operation.title,
                    category = category?.name ?: "Unknown",
                    amount = operation.amount,
                    color = category?.color ?: "#808080",
                    onClickTransaction = { onClickTransaction(operation) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TransactionItem(
    icon: String,
    name: String,
    category: String,
    amount: Double,
    color: String,
    onClickTransaction: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        colors.textSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickTransaction() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = parsedColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = icon,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    style = typography.bodyLarge,
                    color = colors.textPrimary
                )
                Text(
                    text = category,
                    style = typography.bodyMedium,
                    color = colors.textSecondary
                )
            }
        }

        Text(
            text = "${amount.formatSum()} ₽",
            style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = if (amount < 0) colors.expense else colors.income
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.borderLight)
    )
}

@Composable
private fun EmptyBottomSheetState() {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .background(
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "На выбранный день операций нет",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        IconWrapper(
            modifier = Modifier.size(24.dp),
            iconRes = icons.add,
            color = colors.surface
        )
    }
}

@Composable
private fun DayStats(
    operationCount: Int,
    dayBalance: Double
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = LocalContext.current.getQuantityStringRu(
                com.example.sdk.R.plurals.number_of_operations,
                operationCount
            ),
            style = typography.bodyMedium,
            color = colors.textSecondary
        )
        Text(
            text = "${dayBalance.formatSum()} ₽",
            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = if (dayBalance < 0) colors.expense else colors.income
        )
    }
}
