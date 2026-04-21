package com.example.sdk.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sdk.domain.model.Transaction
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.utils.getDateFormat
import com.example.sdk.utils.getQuantityStringRu
import java.util.Calendar

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DayDetailedBottomSheet(
    dayTransactions: List<Transaction>,
    selectedDay: Calendar,
    onClickAdd: () -> Unit,
    onClickTransaction: () -> Unit
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
            transactionCount = dayTransactions.size,
            dayBalance = dayTransactions.sumOf {
                if (it.category.isIncome == true) it.amount else -it.amount
            }
        )

        if (dayTransactions.isEmpty()) {
            EmptyBottomSheetState()
        } else {
            dayTransactions.forEach { transaction ->
                transaction.category.let {
                    val title = if (transaction.note.isNullOrEmpty()) {
                        it.title
                    } else {
                        transaction.note
                    }

                    TransactionItem(
                        icon = it.icon,
                        name = title,
                        category = it.title,
                        amount = transaction.amount * (if (it.isIncome.not()) -1 else 1),
                        color = it.color,
                        onClickTransaction = onClickTransaction
                    )
                }
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
    amount: Long,
    color: Long,
    onClickTransaction: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

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
                        color = Color(color).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    style = typography.titleLarge
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
            color = if (amount < 0) colors.textPrimary else colors.primary
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
    transactionCount: Int,
    dayBalance: Long
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
                transactionCount
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