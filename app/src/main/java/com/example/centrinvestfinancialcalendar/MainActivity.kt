package com.example.centrinvestfinancialcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.sdk.presentation.FinancialCalendarView
import com.example.centrinvestfinancialcalendar.ui.theme.CentrInvestFinancialCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CentrInvestFinancialCalendarTheme {
                FinancialCalendarView()
            }
        }
    }
}