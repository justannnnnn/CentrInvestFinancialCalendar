package com.example.sdk.presentation

import android.os.Build
import com.example.sdk.presentation.calendar.WeekCalendarGrid
import com.example.sdk.presentation.calendar.DayCalendarGrid
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sdk.presentation.bottomnav.BottomNavigationBar
import com.example.sdk.presentation.calendar.MonthCalendarGrid
import com.example.sdk.presentation.components.CalendarHeader
import com.example.sdk.presentation.components.ViewModeTabs
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.White
import com.example.sdk.utils.findActivity

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView(
    viewModel: CalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        context.findActivity()?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = false
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                CalendarSideEffect.OpenAddScreen -> {
                    // навигация
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CalendarHeader(
                calendar = uiState.selectedMonth,
                selectedViewMode = uiState.selectedViewMode,
                onPrevMonth = { viewModel.onAction(CalendarUiAction.OnPrevMonthClick) },
                onNextMonth = { viewModel.onAction(CalendarUiAction.OnNextMonthClick) },
                onAddClick = { viewModel.onAction(CalendarUiAction.OnAddClick) }
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
                tabs = uiState.viewModeTabs,
                selectedMode = uiState.selectedViewMode,
                onModeSelected = { viewModel.onAction(CalendarUiAction.OnViewModeSelected(it)) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray100)
            )

            Spacer(modifier = Modifier.height(4.dp))

            when (uiState.selectedViewMode) {
                ViewModeTab.Month -> MonthCalendarGrid(
                    calendar = uiState.selectedMonth,
                    selectedDay = uiState.selectedDate?.dayOfMonth,
                    daysData = uiState.daysData,
                    onDaySelected = { viewModel.onAction(CalendarUiAction.OnDaySelected(it)) }
                )

                ViewModeTab.Week -> WeekCalendarGrid(
                    calendar = uiState.selectedMonth,
                    selectedDay = uiState.selectedDate?.dayOfMonth,
                    onDaySelected = { viewModel.onAction(CalendarUiAction.OnDaySelected(it))},
                    dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                    dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                )

                ViewModeTab.Day -> DayCalendarGrid(
                    calendar = uiState.selectedMonth,
                    selectedDay = uiState.selectedDate?.dayOfMonth,
                    onDaySelected = { viewModel.onAction(CalendarUiAction.OnDaySelected(it))},
                    dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                    dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                )
            }
        }

        if (uiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onAction(CalendarUiAction.OnDismissBottomSheet) },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = false,
                    confirmValueChange = { true }
                ),
                containerColor = White,
            ) {
                BottomSheetContent(selectedDay = uiState.selectedDate?.dayOfMonth)
            }
        }
    }
}