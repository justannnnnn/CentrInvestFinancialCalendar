package com.example.sdk.presentation.transactions

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kotlin.math.abs

private data class CategoryUi(
    val icon: String,
    val name: String,
    val value: String
)

private enum class RecurringUnit(val label: String) {
    DAYS("дней"),
    WEEKS("недель"),
    MONTHS("месяцев")
}

@Composable
fun CreateOperationScreen(
    initialDate: Calendar,
    onClose: () -> Unit,
    onSave: (
        amount: Long,          // signed, в копейках
        isExpense: Boolean,    // true = расход, false = доход
        category: String,      // value категории
        note: String,          // описание
        isRecurring: Boolean,
        dateEpochMs: Long      // выбранная дата (00:00 локального времени)
    ) -> Unit
) {
    val context = LocalContext.current

    // Каталоги категорий
    val categories = remember {
        listOf(
            CategoryUi("💳", "Покупки", "shopping"),
            CategoryUi("🏠", "Дом и быт", "home"),
            CategoryUi("💸", "Переводы", "transfer"),
            CategoryUi("🛒", "Продукты", "groceries"),
            CategoryUi("🚕", "Транспорт", "transport"),
            CategoryUi("🎬", "Развлечения", "entertainment"),
            CategoryUi("💼", "Работа", "work"),
            CategoryUi("🏥", "Здоровье", "health"),
        )
    }

    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy", Locale("ru")) }

    // Выбранная дата (копия, чтобы не мутировать входной Calendar)
    var selectedDateCal by remember {
        mutableStateOf((initialDate.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        })
    }

    fun openDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selectedDateCal = (selectedDateCal.clone() as Calendar).apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, day)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            },
            selectedDateCal.get(Calendar.YEAR),
            selectedDateCal.get(Calendar.MONTH),
            selectedDateCal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    var categoryValue by remember { mutableStateOf("shopping") }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") } // рубли (может с точкой/запятой)
    var isIncome by remember { mutableStateOf(false) } // true = доход, false = расход
    var isRecurring by remember { mutableStateOf(false) }
    var recurringIntervalText by remember { mutableStateOf("30") }
    var recurringUnit by remember { mutableStateOf(RecurringUnit.DAYS) }
    var recurringTime by remember { mutableStateOf("12:00") }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    val selectedCategory = categories.firstOrNull { it.value == categoryValue } ?: categories.first()

    // Парсинг суммы (в копейки)
    val amountMinor: Long = run {
        val cleaned = amountText.replace(',', '.')
        val dbl = cleaned.toDoubleOrNull() ?: 0.0
        (dbl * 100.0).toLong()
    }

    val saveEnabled = title.isNotBlank() && amountText.isNotBlank() && amountMinor > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = Gray900)
            }
            Text(
                text = "Новая операция",
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                color = Gray900
            )
            Spacer(Modifier.width(40.dp))
        }

        Divider(color = Gray100)

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Дата
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Дата", fontSize = 13.sp, color = Gray500)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Transparent)
                        .clickable { openDatePicker() }
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .borderThin(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(dateFormat.format(selectedDateCal.time), color = Gray900)
                    Text("📅", fontSize = 18.sp)
                }
            }

            // Категория
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Категория", fontSize = 13.sp, color = Gray500)

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Transparent)
                            .clickable { showCategoryDropdown = !showCategoryDropdown }
                            .padding(horizontal = 14.dp, vertical = 14.dp)
                            .borderThin(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedCategory.icon, fontSize = 22.sp)
                            Spacer(Modifier.width(10.dp))
                            Text(selectedCategory.name, color = Gray900)
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Gray500
                        )
                    }

                    if (showCategoryDropdown) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(White)
                                .borderThin()
                        ) {
                            categories.forEach { cat ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            categoryValue = cat.value
                                            showCategoryDropdown = false
                                        }
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(cat.icon, fontSize = 22.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Text(cat.name, color = Gray900)
                                    Spacer(Modifier.weight(1f))
                                    if (cat.value == categoryValue) {
                                        Text("✓", color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                                if (cat != categories.last()) Divider(color = Gray100)
                            }
                        }
                    }
                }
            }

            // Описание
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Описание", fontSize = 13.sp, color = Gray500)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Например: Покупка кофе") }
                )
            }

            // Сумма + −/+
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Сумма", fontSize = 13.sp, color = Gray500)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { newValue ->
                            amountText = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                        },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("0") },
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .borderThin()
                    ) {
                        // Расход (−)
                        Box(
                            modifier = Modifier
                                .clickable { isIncome = false }
                                .background(if (!isIncome) GreenPrimary else White)
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "−",
                                fontSize = 18.sp,
                                color = if (!isIncome) White else Gray500,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp)
                                .background(Gray100)
                        )

                        // Доход (+)
                        Box(
                            modifier = Modifier
                                .clickable { isIncome = true }
                                .background(if (isIncome) GreenPrimary else White)
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "+",
                                fontSize = 18.sp,
                                color = if (isIncome) White else Gray500,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Регулярный платёж
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF6F6F6))
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔁", fontSize = 18.sp)
                    Spacer(Modifier.width(10.dp))
                    Text("Регулярный платёж", modifier = Modifier.weight(1f), color = Gray900)
                    Switch(checked = isRecurring, onCheckedChange = { isRecurring = it })
                }

                if (isRecurring) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF6F6F6))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Повторять каждые", fontSize = 13.sp, color = Gray500)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = recurringIntervalText,
                                onValueChange = { v -> recurringIntervalText = v.filter { it.isDigit() }.take(4) },
                                modifier = Modifier.width(90.dp),
                                singleLine = true
                            )

                            SimpleUnitDropdown(
                                value = recurringUnit,
                                onChange = { recurringUnit = it }
                            )
                        }

                        Text("Время списания", fontSize = 13.sp, color = Gray500)

                        OutlinedTextField(
                            value = recurringTime,
                            onValueChange = { v ->
                                recurringTime = v.take(5).filterIndexed { idx, ch ->
                                    when (idx) {
                                        0, 1, 3, 4 -> ch.isDigit()
                                        2 -> ch == ':'
                                        else -> false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            placeholder = { Text("12:00") }
                        )

                        val nextText = remember(recurringIntervalText, recurringUnit, recurringTime) {
                            buildNextPaymentText(recurringIntervalText, recurringUnit, recurringTime)
                        }
                        Text(
                            text = nextText,
                            fontSize = 12.sp,
                            color = Gray500
                        )
                    }
                }
            }
        }

        // Footer
        Divider(color = Gray100)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val finalMinor = if (isIncome) abs(amountMinor) else -abs(amountMinor)
                    onSave(
                        finalMinor,
                        !isIncome,
                        categoryValue,
                        title,
                        isRecurring,
                        selectedDateCal.timeInMillis
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = saveEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Сохранить", color = White)
            }

            TextButton(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Отмена", color = Gray500)
            }
        }
    }
}

private fun Modifier.borderThin(): Modifier =
    this.then(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
    )

@Composable
private fun SimpleUnitDropdown(
    value: RecurringUnit,
    onChange: (RecurringUnit) -> Unit
) {
    var open by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { open = !open }
                .padding(horizontal = 12.dp, vertical = 14.dp)
                .borderThin(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(value.label, color = Gray900)
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Gray500)
        }

        if (open) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .borderThin()
            ) {
                RecurringUnit.values().forEach { u ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onChange(u)
                                open = false
                            }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(u.label, color = Gray900)
                        Spacer(Modifier.weight(1f))
                        if (u == value) Text("✓", color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    if (u != RecurringUnit.values().last()) Divider(color = Gray100)
                }
            }
        }
    }
}

private fun buildNextPaymentText(intervalText: String, unit: RecurringUnit, time: String): String {
    val interval = intervalText.toIntOrNull()?.coerceAtLeast(1) ?: 30
    val cal = Calendar.getInstance()
    when (unit) {
        RecurringUnit.DAYS -> cal.add(Calendar.DAY_OF_MONTH, interval)
        RecurringUnit.WEEKS -> cal.add(Calendar.DAY_OF_MONTH, interval * 7)
        RecurringUnit.MONTHS -> cal.add(Calendar.MONTH, interval)
    }
    val df = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    val date = df.format(cal.time)
    val t = if (time.length == 5 && time[2] == ':') time else "12:00"
    return "Следующий платёж: $date в $t"
}