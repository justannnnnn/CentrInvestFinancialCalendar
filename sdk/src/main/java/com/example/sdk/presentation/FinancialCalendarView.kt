package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sdk.presentation.bottomnav.BottomNavigationBar
import com.example.sdk.presentation.calendar.DayCalendarGrid
import com.example.sdk.presentation.calendar.MonthCalendarGrid
import com.example.sdk.presentation.calendar.WeekCalendarGrid
import com.example.sdk.presentation.components.CalendarHeader
import com.example.sdk.presentation.components.ViewModeTabs
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.White
import com.example.sdk.utils.findActivity

private data class CalendarAnimatedContentState(
    val viewMode: ViewModeTab,
    val monthTimeMillis: Long
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView(
    viewModel: CalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var navigationDirection by remember { mutableIntStateOf(0) }
    val animationDuration = 280

    LaunchedEffect(Unit) {
        context.findActivity()?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = true
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

    fun goToPreviousPeriod() {
        navigationDirection = -1
        viewModel.onAction(CalendarUiAction.OnPrevMonthClick)
    }

    fun goToNextPeriod() {
        navigationDirection = 1
        viewModel.onAction(CalendarUiAction.OnNextMonthClick)
    }

    val animatedState = CalendarAnimatedContentState(
        viewMode = uiState.selectedViewMode,
        monthTimeMillis = uiState.selectedMonth.timeInMillis
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CalendarHeader(
                calendar = uiState.selectedMonth,
                selectedViewMode = uiState.selectedViewMode,
                onPrevMonth = { goToPreviousPeriod() },
                onNextMonth = { goToNextPeriod() },
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(uiState.selectedViewMode, uiState.selectedMonth.timeInMillis) {
                        var totalDrag = 0f

                        detectHorizontalDragGestures(
                            onDragStart = {
                                totalDrag = 0f
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                totalDrag += dragAmount
                            },
                            onDragEnd = {
                                when {
                                    totalDrag > 120f -> goToPreviousPeriod()
                                    totalDrag < -120f -> goToNextPeriod()
                                }
                            }
                        )
                    }
            ) {
                AnimatedContent(
                    targetState = animatedState,
                    transitionSpec = {
                        val monthChanged = initialState.monthTimeMillis != targetState.monthTimeMillis

                        if (!monthChanged) {
                            EnterTransition.None togetherWith ExitTransition.None
                        } else {
                            val isNext = navigationDirection > 0

                            val enter = slideInHorizontally(
                                animationSpec = tween(animationDuration),
                                initialOffsetX = { fullWidth ->
                                    if (isNext) fullWidth else -fullWidth
                                }
                            ) + fadeIn(
                                animationSpec = tween(animationDuration)
                            )

                            val exit = slideOutHorizontally(
                                animationSpec = tween(animationDuration),
                                targetOffsetX = { fullWidth ->
                                    if (isNext) -fullWidth else fullWidth
                                }
                            ) + fadeOut(
                                animationSpec = tween(animationDuration)
                            )

                            enter togetherWith exit
                        }
                    },
                    label = "calendar_period_animation"
                ) { targetContent ->
                    when (targetContent.viewMode) {
                        ViewModeTab.Month -> MonthCalendarGrid(
                            calendar = uiState.selectedMonth,
                            selectedDay = uiState.selectedDate?.dayOfMonth,
                            daysData = uiState.daysData,
                            onDaySelected = { day ->
                                viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                            }
                        )

                        ViewModeTab.Week -> WeekCalendarGrid(
                            calendar = uiState.selectedMonth,
                            selectedDay = uiState.selectedDate?.dayOfMonth,
                            onDaySelected = { day ->
                                viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                            },
                            dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                            dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                        )

                        ViewModeTab.Day -> DayCalendarGrid(
                            calendar = uiState.selectedMonth,
                            selectedDay = uiState.selectedDate?.dayOfMonth,
                            onDaySelected = { day ->
                                viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                            },
                            dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                            dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                        )
                    }
                }
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
                BottomSheetContent(
                    selectedDay = uiState.selectedDate?.dayOfMonth,
                    selectedMonth = uiState.selectedMonth,
                    transactions = uiState.allMonthTransactions
                )
            }
        }
    }
}