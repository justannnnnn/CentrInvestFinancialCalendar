package com.example.sdk.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BottomSheetContent(selectedDay: Int?) {
    val mockDate = Calendar.getInstance().apply {
        set(2025, Calendar.NOVEMBER, selectedDay ?: 14)
    }
    val dateFormat = SimpleDateFormat("d MMMM, EEEE", Locale("ru"))
    val formattedDate = dateFormat.format(mockDate.time)
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        // Верхняя панель с плюсиком и троеточием
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Плюсик слева
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenPrimary)
                    .clickable { /* Добавить операцию */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Троеточие справа
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray100)
                    .clickable { /* Меню */ },
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

        // Дата
        Text(
            text = formattedDate,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Краткая статистика
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "3 операции",
                fontSize = 14.sp,
                color = Gray500
            )
            Text(
                text = "-2060 ₽",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF95E5A)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Список операций
        TransactionItem(
            icon = "☕",
            name = "Покупка кофе",
            category = "Покупки",
            amount = -230,
            color = 0xFFFFA500
        )

        TransactionItem(
            icon = "🛒",
            name = "Продукты",
            category = "Продукты",
            amount = -1450,
            color = 0xFFFFA500
        )

        TransactionItem(
            icon = "🚕",
            name = "Такси",
            category = "Транспорт",
            amount = -380,
            color = 0xFF1E90FF
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TransactionItem(
    icon: String,
    name: String,
    category: String,
    amount: Int,
    color: Long
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Открыть детали */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Иконка категории
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

            // Название и категория
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

        // Сумма
        Text(
            text = if (amount < 0) "- ${kotlin.math.abs(amount)} ₽" else "+ $amount ₽",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (amount < 0) Gray900 else GreenPrimary
        )
    }

    // Разделитель
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray100)
    )
}