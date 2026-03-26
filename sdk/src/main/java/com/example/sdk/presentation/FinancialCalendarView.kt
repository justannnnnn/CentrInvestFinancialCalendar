package com.example.sdk.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sdk.presentation.bottomnav.BottomNavigationBar
import com.example.sdk.presentation.calendar.DayCalendarGrid
import com.example.sdk.presentation.calendar.MonthCalendarGrid
import com.example.sdk.presentation.calendar.WeekCalendarGrid
import com.example.sdk.presentation.components.CalendarHeader
import com.example.sdk.presentation.components.PeriodSelectorDialog
import com.example.sdk.presentation.components.ViewModeTabs
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.presentation.transactions.CreateOperationScreen
import com.example.sdk.ui.theme.Gray100
import com.example.sdk.ui.theme.Gray500
import com.example.sdk.ui.theme.White
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView() {
    val context = LocalContext.current

    val viewModel: CalendarViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        context.findActivity()?.window?.let { window ->
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = false
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    // UI-only state (показы модалок)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showCreateSheet by remember { mutableStateOf(false) }
    var showPeriodDialog by remember { mutableStateOf(false) }

    // YearMonth -> Calendar
    val currentMonthCal = remember(
        uiState.currentYearMonth,
        uiState.selectedDate,
        uiState.selectedViewMode
    ) {
        Calendar.getInstance().apply {
            val baseDate = when (uiState.selectedViewMode) {
                ViewModeTab.Month -> uiState.currentYearMonth.atDay(1)
                ViewModeTab.Week, ViewModeTab.Day -> uiState.selectedDate ?: LocalDate.now()
            }

            set(Calendar.YEAR, baseDate.year)
            set(Calendar.MONTH, baseDate.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, baseDate.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    val selectedDayInt: Int? = uiState.selectedDate?.dayOfMonth

    val selectedViewModeString = when (uiState.selectedViewMode) {
        ViewModeTab.Month -> "month"
        ViewModeTab.Week -> "week"
        ViewModeTab.Day -> "day"
    }

    // Дата по умолчанию для экрана создания: выбранная в календаре или сегодня
    val initialCreateCal = remember(uiState.selectedDate) {
        Calendar.getInstance().apply {
            val d = uiState.selectedDate ?: LocalDate.now()
            set(d.year, d.monthValue - 1, d.dayOfMonth, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CalendarHeader(
                calendar = currentMonthCal,
                selectedViewMode = selectedViewModeString,
                onPrevMonth = {
                    when (uiState.selectedViewMode) {
                        ViewModeTab.Month -> viewModel.onPrevMonth()
                        ViewModeTab.Week -> viewModel.onPrevWeek()
                        ViewModeTab.Day -> viewModel.onPrevDay()
                    }
                },
                onNextMonth = {
                    when (uiState.selectedViewMode) {
                        ViewModeTab.Month -> viewModel.onNextMonth()
                        ViewModeTab.Week -> viewModel.onNextWeek()
                        ViewModeTab.Day -> viewModel.onNextDay()
                    }
                },
                onAddClick = { showCreateSheet = true },
                onPeriodClick = { showPeriodDialog = true }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = "calendar",
                onTabSelected = { /* заглушка */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ViewModeTabs(
                selectedMode = selectedViewModeString,
                onModeSelected = { modeString ->
                    val mode = when (modeString) {
                        "month" -> ViewModeTab.Month
                        "week" -> ViewModeTab.Week
                        "day" -> ViewModeTab.Day
                        else -> ViewModeTab.Month
                    }
                    viewModel.onChangeViewMode(mode)

                    if ((mode == ViewModeTab.Week || mode == ViewModeTab.Day) && uiState.selectedDate == null) {
                        viewModel.onSelectDate(LocalDate.now())
                    }
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray100)
            )

            Spacer(modifier = Modifier.height(4.dp))

            when (selectedViewModeString) {
                "month" -> {
                    MonthCalendarGrid(
                        calendar = currentMonthCal,
                        selectedDay = selectedDayInt,
                        onDaySelected = { day: Int ->
                            val date: LocalDate = uiState.currentYearMonth.atDay(day)
                            viewModel.onSelectDate(date)
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }

                "week" -> {
                    WeekCalendarGrid(
                        calendar = currentMonthCal,
                        selectedDay = selectedDayInt,
                        onDaySelected = { day: Int ->
                            val date: LocalDate = uiState.currentYearMonth.atDay(day)
                            viewModel.onSelectDate(date)
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }

                "day" -> {
                    DayCalendarGrid(
                        calendar = currentMonthCal,
                        selectedDay = selectedDayInt,
                        onDaySelected = { day: Int ->
                            val date: LocalDate = uiState.currentYearMonth.atDay(day)
                            viewModel.onSelectDate(date)
                            showBottomSheet = true
                        },
                        dayHasOperations = { day: Int -> day % 5 == 0 || day % 3 == 0 },
                        dayHasRecurring = { day: Int -> day == 1 || day == 10 }
                    )
                }

                else -> {
                    Text(
                        text = "${selectedViewModeString.replaceFirstChar { it.uppercase() }} — в разработке",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Gray500
                    )
                }
            }
        }

        // BottomSheet дня
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = false,
                    confirmValueChange = { true }
                ),
                containerColor = White,
            ) {
                BottomSheetContent(
                    calendar = currentMonthCal,
                    selectedDay = selectedDayInt,
                    onAddClick = { showCreateSheet = true }
                )
            }
        }

        // Create Operation (full screen)
        if (showCreateSheet) {
            Dialog(
                onDismissRequest = { showCreateSheet = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(White)
                ) {
                    CreateOperationScreen(
                        initialDate = initialCreateCal,
                        onClose = { showCreateSheet = false },
                        onSave = { amountSignedMinor, isExpense, category, note, isRecurring, dateEpochMs ->
                            val date = Instant.ofEpochMilli(dateEpochMs)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            viewModel.addTransaction(
                                amountMinorSigned = amountSignedMinor,
                                category = category,
                                note = note,
                                date = date,
                                isRecurring = isRecurring
                            )

                            showCreateSheet = false
                            showBottomSheet = false
                        }
                    )
                }
            }
        }

        // Period selector
        if (showPeriodDialog) {
            val initStartCal = remember(uiState.periodStart) {
                Calendar.getInstance().apply {
                    val d = uiState.periodStart
                    set(d.year, d.monthValue - 1, d.dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }
            val initEndCal = remember(uiState.periodEnd) {
                Calendar.getInstance().apply {
                    val d = uiState.periodEnd
                    set(d.year, d.monthValue - 1, d.dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
            }

            PeriodSelectorDialog(
                initialStart = initStartCal,
                initialEnd = initEndCal,
                onClose = { showPeriodDialog = false },
                onApply = { start, end ->
                    val zone = ZoneId.systemDefault()
                    val startLd = Instant.ofEpochMilli(start.timeInMillis).atZone(zone).toLocalDate()
                    val endLd = Instant.ofEpochMilli(end.timeInMillis).atZone(zone).toLocalDate()
                    viewModel.onChangePeriod(startLd, endLd)
                    showPeriodDialog = false
                }
            )
        }
    }
}