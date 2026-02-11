package com.example.sdk.presentation.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun AllTransactionsHeader(
    onViewAllClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Все операции",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
        Text(
            text = "Смотреть все",
            fontSize = 14.sp,
            color = GreenPrimary,
            modifier = Modifier.clickable { onViewAllClick() }
        )
    }
}
