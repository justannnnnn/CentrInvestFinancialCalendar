package com.example.sdk.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.sdk.presentation.bottomnav.BottomNavigationBar
import com.example.sdk.presentation.calendar.MonthCalendarGrid
import com.example.sdk.presentation.calendar.WeekCalendarGrid
import com.example.sdk.presentation.calendar.DayCalendarGrid
import com.example.sdk.presentation.components.CalendarHeader
import com.example.sdk.presentation.components.ViewModeTabs
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.White
import java.util.Calendar

// Extension для поиска Activity
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.findActivity()?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = false
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var selectedViewMode by remember { mutableStateOf("month") }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CalendarHeader(
                calendar = currentMonth,
                selectedViewMode = selectedViewMode,
                onPrevMonth = {
                    currentMonth = currentMonth.apply { add(Calendar.MONTH, -1) }
                },
                onNextMonth = {
                    currentMonth = currentMonth.apply { add(Calendar.MONTH, 1) }
                },
                onAddClick = { /* TODO: открыть добавление */ }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "calendar",
                onTabSelected = { /* пока заглушка */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ViewModeTabs(
                selectedMode = selectedViewMode,
                onModeSelected = { mode -> selectedViewMode = mode }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray100)
            )

            Spacer(modifier = Modifier.height(4.dp))

//            if (selectedViewMode == "month") {
//                MonthCalendarGrid(
//                    calendar = currentMonth,
//                    selectedDay = selectedDay,
//                    onDaySelected = { day: Int ->
//                        selectedDay = day
//                        showBottomSheet = true
//                    },
//                    dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
//                    dayHasRecurring = { day: Int -> day == 1 || day == 10 }
//                )
//            } else {
//                Text(
//                    text = "${selectedViewMode.replaceFirstChar { it.uppercase() }} — в разработке",
//                    modifier = Modifier.padding(16.dp),
//                    fontSize = 16.sp,
//                    color = Gray500
//                )
//            }
            when (selectedViewMode) {
                "month" -> {
                    MonthCalendarGrid(
                        calendar = currentMonth,
                        selectedDay = selectedDay,
                        onDaySelected = { day: Int ->
                            selectedDay = day
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }
                "week" -> {
                    WeekCalendarGrid(
                        calendar = currentMonth,
                        selectedDay = selectedDay,
                        onDaySelected = { day: Int ->
                            selectedDay = day
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }
                "day" -> {
                    DayCalendarGrid(
                        calendar = currentMonth,
                        selectedDay = selectedDay,
                        onDaySelected = { day: Int ->
                            selectedDay = day
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }
                else -> {
                    Text(
                        text = "${selectedViewMode.replaceFirstChar { it.uppercase() }} — в разработке",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Gray500
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = false,
                    confirmValueChange = { true }
                ),
                containerColor = White,
            ) {
                BottomSheetContent(selectedDay = selectedDay)
            }
        }
    }
}