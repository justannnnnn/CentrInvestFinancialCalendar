package com.example.sdk.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun StatsSummaryBlock() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Доходы",
            fontSize = 14.sp,
            color = Gray500
        )
        Text(
            text = "80 000 ₽",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Расходы",
            fontSize = 14.sp,
            color = Gray500
        )
        Text(
            text = "-24 570 ₽",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF95E5A)
        )
    }
}
