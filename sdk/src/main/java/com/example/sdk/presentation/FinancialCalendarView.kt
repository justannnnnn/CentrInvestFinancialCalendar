package com.example.sdk.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun FinancialCalendarView(viewModel: CalendarViewModel = hiltViewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Добавить транзакцию",
            fontSize = 20.sp
        )
        OutlinedTextField(
            value = viewModel.amountInput,
            onValueChange = { viewModel.amountInput = it },
            label = { Text("Сумма (руб)") }
        )

        OutlinedTextField(
            value = viewModel.categoryInput,
            onValueChange = { viewModel.categoryInput = it },
            label = { Text("Категория") }
        )

        OutlinedTextField(
            value = viewModel.noteInput,
            onValueChange = { viewModel.noteInput = it },
            label = { Text("Комментарий (необязательно)") }
        )

        Button(onClick = { viewModel.addTransaction() }) {
            Text("Добавить")
        }

        Spacer(Modifier.height(24.dp))

        Text("Список транзакций", fontSize = 20.sp)

        LazyColumn {
            items(viewModel.transactions) { tx ->
                Text("${tx.category}: ${tx.amount / 100.0}₽ — ${tx.note ?: ""}")
                Divider()
            }
        }
    }
}