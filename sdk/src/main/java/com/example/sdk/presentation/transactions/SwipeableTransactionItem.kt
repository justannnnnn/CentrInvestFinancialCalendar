package com.example.sdk.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.domain.model.Category
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import kotlin.math.abs
import kotlin.math.roundToInt

data class SwipeableTransaction(
    val id: Int,
    val name: String,
    val amount: Long,
    val category: Category
)

@Composable
fun SwipeableTransactionItem(
    transaction: SwipeableTransaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = 60f
    val maxOffset = 80f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        // Кнопка редактирования (слева)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(80.dp)
                .background(Color(0xFFFFB020))
                .clickable {
                    offsetX = 0f
                    onEdit()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Редактировать",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Кнопка удаления (справа)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(80.dp)
                .background(Color(0xFFF95E5A))
                .clickable {
                    offsetX = 0f
                    onDelete()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Контент операции (свайпаемый)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(White)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { offsetX = 0f },
                        onDragEnd = {
                            offsetX = when {
                                offsetX > swipeThreshold -> maxOffset
                                offsetX < -swipeThreshold -> -maxOffset
                                else -> 0f
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceIn(-maxOffset, maxOffset)
                        }
                    )
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Открыть детали */ }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
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
                            .background(Color(transaction.category.color).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = transaction.category.icon,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = transaction.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray900
                        )
                        Text(
                            text = transaction.category.title,
                            fontSize = 14.sp,
                            color = Gray500
                        )
                    }
                }
                val isIncome = transaction.category.isIncome

                Text(
                    text = "${if (isIncome) "+" else "-"} ${transaction.amount.formatSum()} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isIncome) GreenPrimary else Gray900
                )
            }
        }
    }
}