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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.ui.theme.Gray900
import com.example.sdk.ui.theme.GreenPrimary

@Composable
fun AllTransactionsHeader(
    onViewAllClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp, top = 24.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.all_transactions),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
        Text(
            text = stringResource(R.string.look_all),
            fontSize = 14.sp,
            color = GreenPrimary,
            modifier = Modifier.clickable { onViewAllClick() }
        )
    }
}
