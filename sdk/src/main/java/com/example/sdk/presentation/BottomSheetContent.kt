package com.example.sdk.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun BottomSheetContent(selectedDay: Int?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Детали дня: $selectedDay",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Операция 1: Зарплата 80 000 ₽", fontSize = 16.sp)
        Text("Операция 2: Расход -1 000 ₽", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Сбросить фильтр", color = GreenPrimary, fontSize = 16.sp)
    }
}