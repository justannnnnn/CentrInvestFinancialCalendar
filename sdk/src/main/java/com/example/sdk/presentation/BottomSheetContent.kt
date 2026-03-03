package com.example.sdk.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.domain.model.Transaction
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DayDetailedBottomSheet(
    dayTransactions: List<Transaction>,
    selectedDay: Calendar,
    onClickAdd: () -> Unit,
    onClickMenu: () -> Unit,
    onClickTransaction: () -> Unit
) {
    val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
    val formattedDate = dateFormat.format(selectedDay.time)
        .replaceFirstChar { it.uppercase() }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (LocalConfiguration.current.screenHeightDp * 0.7).dp)
            .verticalScroll(scrollState)
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlusButton(onClick = onClickAdd)
            MoreButton(onClick = onClickMenu)
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = formattedDate,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )

        DayStats(
            transactionCount = dayTransactions.size,
            dayBalance = dayTransactions.sumOf {
                if (it.category?.isIncome == true) it.amount else -it.amount
            }
        )

        if (dayTransactions.isEmpty()) {
            EmptyBottomSheetState()
        } else {
            dayTransactions.forEach { transaction ->
                transaction.category?.let {
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
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(color).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray900
                )
                Text(
                    text = category,
                    fontSize = 14.sp,
                    color = Gray500
                )
            }
        }

        Text(
            text = "${amount.formatSum()} ₽",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (amount < 0) Gray900 else GreenPrimary
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}

@Composable
private fun EmptyBottomSheetState() {
    // tODO
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(GreenPrimary)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        IconWrapper(
            modifier = Modifier.size(24.dp),
            iconRes = R.drawable.plus,
            color = White
        )
    }
}

@Composable
private fun MoreButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Меню",
            tint = Gray900,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun DayStats(
    transactionCount: Int,
    dayBalance: Long
) {
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
                R.plurals.number_of_operations,
                transactionCount,
                transactionCount
            ),
            fontSize = 14.sp,
            color = Gray500
        )
        Text(
            text = "${dayBalance.formatSum()} ₽",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (dayBalance < 0) Color(0xFFF95E5A) else Color(0xFF10B981)
        )
    }
}

fun Context.getQuantityStringRu(id: Int, quantity: Int, vararg formatArgs: Any): String {
    return getContextRu().resources.getQuantityString(id, quantity, *formatArgs)
}

fun Context.getContextRu(): Context {
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(Locale("ru", "RU"))
    return createConfigurationContext(configuration)
}

