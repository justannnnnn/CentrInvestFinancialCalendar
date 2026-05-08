package com.example.sdk.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.presentation.statistics.formatSum
import com.example.sdk.ui.theme.CalendarTheme
import kotlin.math.roundToInt

data class SwipeableTransaction(
    val id: Int,
    val name: String,
    val amount: Long,
    val category: CalendarCategoryUi
)

@Composable
fun SwipeableTransactionItem(
    transaction: SwipeableTransaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = CalendarTheme.colors

    var offsetX by remember { mutableFloatStateOf(0f) }

    val swipeThreshold = 72f
    val maxOffset = 96f
    val cardShape = RoundedCornerShape(16.dp)

    val parsedCategoryColor = try {
        Color(android.graphics.Color.parseColor(transaction.category.color))
    } catch (e: Exception) {
        colors.textSecondary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(
                color = colors.borderLight,
                shape = cardShape
            )
    ) {
        Row(
            modifier = Modifier.matchParentSize()
        ) {
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
                        color = colors.expense,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = colors.expense,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 8.dp)
                .size(width = 88.dp, height = 60.dp)
                .background(Color.Transparent, RoundedCornerShape(14.dp))
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
                .background(Color.Transparent, RoundedCornerShape(14.dp))
                .clickable(enabled = offsetX <= -maxOffset * 0.9f) {
                    offsetX = 0f
                    onDelete()
                }
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .fillMaxWidth()
                .height(76.dp)
                .background(
                    color = colors.surface,
                    shape = cardShape
                )
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
                    .clickable { }
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
                                color = parsedCategoryColor.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = transaction.category.iconUrl,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(12.dp))

                    Column {
                        Text(
                            text = transaction.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary
                        )
                        Text(
                            text = transaction.category.name,
                            fontSize = 14.sp,
                            color = colors.textSecondary
                        )
                    }
                }

                val isIncome = transaction.category.isIncome

                Text(
                    text = "${if (isIncome) "+" else "-"} ${(transaction.amount / 100.0).formatSum()} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isIncome) colors.primary else colors.textPrimary
                )
            }
        }
    }
}
