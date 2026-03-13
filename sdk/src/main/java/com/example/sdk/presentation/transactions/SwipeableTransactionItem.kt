package com.example.sdk.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary
import com.example.sdk.ui.theme.White
import kotlin.math.abs
import kotlin.math.roundToInt

data class SwipeableTransaction(
    val id: Int,
    val name: String,
    val amount: Int,
    val category: String,
    val icon: String,
    val color: Long
)

@Composable
fun SwipeableTransactionItem(
    transaction: SwipeableTransaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    val swipeThreshold = 72f
    val maxOffset = 96f
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .clip(cardShape)
            .background(Color(0xFFF7F7F8))
    ) {
        // Фон действий
        Row(
            modifier = Modifier
                .matchParentSize()
        ) {
            // Левая зона (редактировать)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(start = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редактировать",
                        tint = Color(0xFFE69500),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Изменить",
                        color = Color(0xFFE69500),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Правая зона (удалить)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFECEB)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    modifier = Modifier.padding(end = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Удалить",
                        color = Color(0xFFF95E5A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color(0xFFF95E5A),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Нажимаемые action-зоны
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 8.dp)
                .size(width = 88.dp, height = 60.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable(enabled = offsetX >= maxOffset * 0.9f) {
                    offsetX = 0f
                    onEdit()
                }
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .padding(end = 8.dp)
                .size(width = 88.dp, height = 60.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable(enabled = offsetX <= -maxOffset * 0.9f) {
                    offsetX = 0f
                    onDelete()
                }
        )

        // Белая карточка поверх
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .height(76.dp)
                .clip(cardShape)
                .background(White)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
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
                            .background(Color(transaction.color).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = transaction.icon,
                            fontSize = 24.sp
                        )
                    }

                    androidx.compose.foundation.layout.Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column {
                        Text(
                            text = transaction.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray900
                        )
                        Text(
                            text = transaction.category,
                            fontSize = 14.sp,
                            color = Gray500
                        )
                    }
                }

                Text(
                    text = if (transaction.amount < 0) {
                        "- ${abs(transaction.amount)} ₽"
                    } else {
                        "+ ${transaction.amount} ₽"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (transaction.amount < 0) Gray900 else GreenPrimary
                )
            }
        }
    }
}