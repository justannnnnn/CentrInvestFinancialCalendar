package com.example.sdk.presentation.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.border

@Composable
fun PeriodSelectorDialog(
    initialStart: Calendar,
    initialEnd: Calendar,
    onClose: () -> Unit,
    onApply: (start: Calendar, end: Calendar) -> Unit
) {
    val context = LocalContext.current

    var start by remember { mutableStateOf(initialStart.clone() as Calendar) }
    var end by remember { mutableStateOf(initialEnd.clone() as Calendar) }

    val dateFormat = remember {
        SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    }

    fun quickSelect(days: Int) {
        val e = Calendar.getInstance()
        val s = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -days)
        }
        start = s
        end = e
    }

    fun openDatePicker(calendar: Calendar, onSelected: (Calendar) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val newCal = (calendar.clone() as Calendar).apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onSelected(newCal)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Затемнение
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        // Карточка
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .widthIn(max = 400.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(White)
                .clickable(enabled = false) { },
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Выбор периода",
                    fontSize = 16.sp,
                    color = Gray900,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = Gray500
                    )
                }
            }

            Divider(color = Gray100)

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text("Быстрый выбор", fontSize = 12.sp, color = Gray500)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickButton("7 дней", Modifier.weight(1f)) { quickSelect(7) }
                    QuickButton("30 дней", Modifier.weight(1f)) { quickSelect(30) }
                    QuickButton("90 дней", Modifier.weight(1f)) { quickSelect(90) }
                }


                Text("Или выберите даты", fontSize = 12.sp, color = Gray500)

                DateField(
                    label = "Начало периода",
                    value = dateFormat.format(start.time)
                ) {
                    openDatePicker(start) { start = it }
                }

                DateField(
                    label = "Конец периода",
                    value = dateFormat.format(end.time)
                ) {
                    openDatePicker(end) { end = it }
                }

                Button(
                    onClick = {
                        if (start.after(end)) {
                            onApply(end, start)
                        } else {
                            onApply(start, end)
                        }
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Применить", color = White)
                }
            }
        }
    }
}

@Composable
private fun QuickButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Gray900, fontSize = 13.sp)
    }
}


@Composable
private fun DateField(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontSize = 12.sp, color = Gray500)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .border(1.dp, Gray100, RoundedCornerShape(12.dp))
        ) {
            Text(value, color = Gray900)
        }
    }
}
