package com.example.sdk.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sdk.presentation.add.AddTransactionBottomSheet
import com.example.sdk.presentation.bottomnav.BottomNavigationBar
import com.example.sdk.presentation.calendar.DayCalendarGrid
import com.example.sdk.presentation.calendar.MonthCalendarGrid
import com.example.sdk.presentation.calendar.WeekCalendarGrid
import com.example.sdk.presentation.components.CalendarHeader
import com.example.sdk.presentation.components.ThemePickerBottomSheet
import com.example.sdk.presentation.components.ViewModeTabs
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeSelection
import com.example.sdk.presentation.models.ViewModeTab
import com.example.sdk.presentation.themeeditor.PartialThemeEditorScreen
import com.example.sdk.sdk.CalendarSdk
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemeExamples
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarThemeStorage
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.FinancialCalendarTheme
import com.example.sdk.utils.findActivity
import com.example.sdk.presentation.components.ThemeCreationModeSheet
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.presentation.themeeditor.FullThemeEditorScreen
import java.util.Locale

private data class CalendarAnimatedContentState(
    val viewMode: ViewModeTab,
    val periodTimeMillis: Long
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalendarView(
    viewModel: CalendarViewModel = viewModel(),
    themeConfig: CalendarThemeConfig = CalendarSdk.calendarThemeConfig
) {
    val context = LocalContext.current

    val initialThemeSelection = remember(context, themeConfig) {
        CalendarThemeStorage.getThemeSelection(context)
            ?: ThemeSelection.Preset(themeConfig.preset)
    }

    val initialThemeConfig = remember(context, themeConfig, initialThemeSelection) {
        resolveInitialThemeConfig(
            context = context,
            fallbackThemeConfig = themeConfig,
            selection = initialThemeSelection
        )
    }

    var activeThemeSelection by remember(initialThemeSelection) {
        mutableStateOf(initialThemeSelection)
    }

    var activeThemeConfig by remember(initialThemeConfig) {
        mutableStateOf(initialThemeConfig)
    }

    var showThemePickerSheet by remember { mutableStateOf(false) }
    var showThemeCreationModeSheet by remember { mutableStateOf(false) }
    var showPartialEditorSheet by remember { mutableStateOf(false) }
    var showFullEditorSheet by remember { mutableStateOf(false) }
    var editingCustomTheme by remember { mutableStateOf<CustomCalendarTheme?>(null) }
    var customThemesVersion by remember { mutableIntStateOf(0) }
    var showDiscardChangesDialog by remember { mutableStateOf(false) }
    var pendingEditorCloseAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val customThemes = remember(customThemesVersion, context) {
        CalendarThemeStorage.getCustomThemes(context)
    }

    val partialEditorSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            if (newValue == SheetValue.Hidden && showPartialEditorSheet) {
                pendingEditorCloseAction = {
                    showPartialEditorSheet = false
                    editingCustomTheme = null
                }
                showDiscardChangesDialog = true
                false
            } else {
                true
            }
        }
    )

    val fullEditorSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            if (newValue == SheetValue.Hidden && showFullEditorSheet) {
                pendingEditorCloseAction = {
                    showFullEditorSheet = false
                    editingCustomTheme = null
                }
                showDiscardChangesDialog = true
                false
            } else {
                true
            }
        }
    )

    FinancialCalendarTheme(themeConfig = activeThemeConfig) {
        val colors = CalendarTheme.colors
        val uiState by viewModel.uiState.collectAsState()

        var navigationDirection by remember { mutableIntStateOf(0) }
        val animationDuration = 180

        LaunchedEffect(colors.surface) {
            context.findActivity()?.window?.let { window ->
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.isAppearanceLightStatusBars = colors.surface.luminance() > 0.5f
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.statusBarColor = android.graphics.Color.TRANSPARENT
            }
        }

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    CalendarSideEffect.OpenAddScreen -> {
                        // TODO: navigation
                    }
                }
            }
        }

        fun applyPresetTheme(preset: CalendarThemePreset) {
            val newThemeConfig = when (preset) {
                CalendarThemePreset.Default -> CalendarThemeExamples.defaultTheme
                CalendarThemePreset.Ocean -> CalendarThemeExamples.oceanTheme
                CalendarThemePreset.Graphite -> CalendarThemeExamples.graphiteTheme
            }

            activeThemeSelection = ThemeSelection.Preset(preset)
            activeThemeConfig = newThemeConfig

            CalendarThemeStorage.saveThemeSelection(
                context = context,
                selection = ThemeSelection.Preset(preset)
            )
            CalendarSdk.updateTheme(newThemeConfig)

            showThemePickerSheet = false
        }

        fun applyCustomTheme(theme: CustomCalendarTheme) {
            activeThemeSelection = ThemeSelection.Custom(theme.id)
            activeThemeConfig = theme.themeConfig

            CalendarThemeStorage.saveThemeSelection(
                context = context,
                selection = ThemeSelection.Custom(theme.id)
            )
            CalendarSdk.updateTheme(theme.themeConfig)

            showThemePickerSheet = false
        }

        fun deleteCustomTheme(theme: CustomCalendarTheme) {
            CalendarThemeStorage.deleteCustomTheme(context, theme.id)
            customThemesVersion++

            val isDeletedThemeActive =
                activeThemeSelection is ThemeSelection.Custom &&
                        (activeThemeSelection as ThemeSelection.Custom).themeId == theme.id

            if (isDeletedThemeActive) {
                val fallbackSelection = ThemeSelection.Preset(CalendarThemePreset.Default)
                val fallbackTheme = CalendarThemeExamples.defaultTheme

                activeThemeSelection = fallbackSelection
                activeThemeConfig = fallbackTheme

                CalendarThemeStorage.saveThemeSelection(
                    context = context,
                    selection = fallbackSelection
                )

                CalendarSdk.updateTheme(fallbackTheme)
            }
        }

        fun duplicateCustomTheme(theme: CustomCalendarTheme) {
            val now = System.currentTimeMillis()
            val duplicatedTheme = theme.copy(
                id = java.util.UUID.randomUUID().toString(),
                name = "${theme.name} (копия)",
                createdAt = now,
                updatedAt = now
            )

            CalendarThemeStorage.upsertCustomTheme(
                context = context,
                theme = duplicatedTheme
            )
            customThemesVersion++
        }

        fun openThemeCreationMode() {
            editingCustomTheme = null
            showThemePickerSheet = false
            showThemeCreationModeSheet = true
        }

        fun openCreatePartialTheme() {
            editingCustomTheme = null
            showThemeCreationModeSheet = false
            showPartialEditorSheet = true
        }

        fun openCreateFullTheme() {
            editingCustomTheme = null
            showThemeCreationModeSheet = false
            showFullEditorSheet = true
        }

        fun openEditTheme(theme: CustomCalendarTheme) {
            editingCustomTheme = theme
            showThemePickerSheet = false

            when (theme.mode) {
                ThemeCustomizationMode.PARTIAL -> {
                    showPartialEditorSheet = true
                }
                ThemeCustomizationMode.FULL -> {
                    showFullEditorSheet = true
                }
            }
        }

        fun goToPreviousPeriod() {
            navigationDirection = -1
            viewModel.onAction(CalendarUiAction.OnPrevPeriodClick)
        }

        fun goToNextPeriod() {
            navigationDirection = 1
            viewModel.onAction(CalendarUiAction.OnNextPeriodClick)
        }

        val animatedState = CalendarAnimatedContentState(
            viewMode = uiState.selectedViewMode,
            periodTimeMillis = uiState.selectedPeriod.timeInMillis
        )

        Scaffold(
            containerColor = colors.background,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surface)
                        .statusBarsPadding()
                ) {
                    CalendarHeader(
                        calendar = uiState.selectedPeriod,
                        selectedViewMode = uiState.selectedViewMode,
                        activeThemeSelection = activeThemeSelection,
                        activeThemePrimaryColor = colors.primary,
                        onPrevMonth = { goToPreviousPeriod() },
                        onNextMonth = { goToNextPeriod() },
                        onThemeClick = { showThemePickerSheet = true },
                        onAddClick = { viewModel.onAction(CalendarUiAction.OnAddClick) }
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = "calendar",
                    onTabSelected = { }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colors.background)
            ) {
                ViewModeTabs(
                    tabs = uiState.viewModeTabs,
                    selectedMode = uiState.selectedViewMode,
                    onModeSelected = { mode ->
                        viewModel.onAction(CalendarUiAction.OnViewModeSelected(mode))
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.borderLight)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(
                            uiState.selectedViewMode,
                            uiState.selectedPeriod.timeInMillis
                        ) {
                            var totalDrag = 0f

                            detectHorizontalDragGestures(
                                onDragStart = { totalDrag = 0f },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    totalDrag += dragAmount
                                },
                                onDragEnd = {
                                    when {
                                        totalDrag > 60f -> goToPreviousPeriod()
                                        totalDrag < -60f -> goToNextPeriod()
                                    }
                                }
                            )
                        }
                ) {
                    AnimatedContent(
                        targetState = animatedState,
                        transitionSpec = {
                            val periodChanged =
                                initialState.periodTimeMillis != targetState.periodTimeMillis

                            if (!periodChanged) {
                                EnterTransition.None togetherWith ExitTransition.None
                            } else {
                                val isNext = navigationDirection > 0

                                val enter = slideInHorizontally(
                                    animationSpec = tween(animationDuration),
                                    initialOffsetX = { fullWidth ->
                                        if (isNext) fullWidth else -fullWidth
                                    }
                                ) + fadeIn(animationSpec = tween(animationDuration))

                                val exit = slideOutHorizontally(
                                    animationSpec = tween(animationDuration),
                                    targetOffsetX = { fullWidth ->
                                        if (isNext) -fullWidth else fullWidth
                                    }
                                ) + fadeOut(animationSpec = tween(animationDuration))

                                enter togetherWith exit
                            }
                        },
                        label = "calendar_period_animation"
                    ) { targetContent ->
                        when (targetContent.viewMode) {
                            ViewModeTab.Month -> {
                                MonthCalendarGrid(
                                    calendar = uiState.selectedPeriod,
                                    selectedDay = uiState.selectedDate?.get(java.util.Calendar.DAY_OF_MONTH),
                                    daysData = uiState.daysData,
                                    onDaySelected = { day ->
                                        viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                                    }
                                )
                            }

                            ViewModeTab.Week -> {
                                WeekCalendarGrid(
                                    calendar = uiState.selectedPeriod,
                                    selectedDay = uiState.selectedDate?.get(java.util.Calendar.DAY_OF_MONTH),
                                    daysData = uiState.daysData,
                                    onDaySelected = { day ->
                                        viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                                    }
                                )
                            }

                            ViewModeTab.Day -> {
                                val selectedDay =
                                    uiState.selectedDate?.get(java.util.Calendar.DAY_OF_MONTH)

                                val dayTransactions = selectedDay?.let { day ->
                                    uiState.daysData[day]?.transactions
                                } ?: emptyList()

                                DayCalendarGrid(
                                    calendar = uiState.selectedPeriod,
                                    selectedDay = selectedDay,
                                    transactions = dayTransactions,
                                    onDaySelected = { day ->
                                        viewModel.onAction(CalendarUiAction.OnDaySelected(day))
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        viewModel.onAction(CalendarUiAction.OnDismissBottomSheet)
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = false,
                        confirmValueChange = { true }
                    ),
                    containerColor = colors.surface
                ) {
                    DayDetailedBottomSheet(
                        dayTransactions = uiState.allMonthTransactions,
                        selectedDay = uiState.selectedDate ?: uiState.selectedPeriod,
                        onClickAdd = { viewModel.onAction(CalendarUiAction.OnAddClick) },
                        onClickTransaction = { }
                    )
                }
            }

            if (uiState.isAddTransactionVisible) {
                AddTransactionBottomSheet(
                    onDismiss = { viewModel.onAddDismiss() }
                )
            }

            if (showThemePickerSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showThemePickerSheet = false
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                        confirmValueChange = { true }
                    ),
                    containerColor = colors.surface
                ) {
                    ThemePickerBottomSheet(
                        selectedThemeSelection = activeThemeSelection,
                        customThemes = customThemes,
                        onPresetSelected = { preset ->
                            applyPresetTheme(preset)
                        },
                        onCustomThemeSelected = { theme ->
                            applyCustomTheme(theme)
                        },
                        onCreateTheme = {
                            openThemeCreationMode()
                        },
                        onEditCustomTheme = { theme ->
                            openEditTheme(theme)
                        },
                        onDeleteCustomTheme = { theme ->
                            deleteCustomTheme(theme)
                        },
                        onDismiss = {
                            showThemePickerSheet = false
                        },
                        onDuplicateCustomTheme = { theme ->
                            duplicateCustomTheme(theme)
                        },
                    )
                }
            }

            if (showThemeCreationModeSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showThemeCreationModeSheet = false
                    },
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                        confirmValueChange = { true }
                    ),
                    containerColor = colors.surface
                ) {
                    ThemeCreationModeSheet(
                        onModeSelected = { mode ->
                            when (mode) {
                                ThemeCustomizationMode.PARTIAL -> openCreatePartialTheme()
                                ThemeCustomizationMode.FULL -> openCreateFullTheme()
                            }
                        },
                        onDismiss = {
                            showThemeCreationModeSheet = false
                        }
                    )
                }
            }

            if (showPartialEditorSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        pendingEditorCloseAction = {
                            showPartialEditorSheet = false
                            editingCustomTheme = null
                        }
                        showDiscardChangesDialog = true
                    },
                    sheetState = partialEditorSheetState,
                    containerColor = colors.surface
                ) {
                    PartialThemeEditorScreen(
                        initialTheme = editingCustomTheme,
                        defaultBasePreset = activeThemeConfig.preset,
                        editorThemeConfig = activeThemeConfig,
                        onDismiss = {
                            showPartialEditorSheet = false
                            editingCustomTheme = null
                        },
                        onSave = { savedTheme ->
                            val normalizedTheme = savedTheme.copy(
                                name = makeUniqueThemeName(
                                    desiredName = savedTheme.name,
                                    existingThemes = customThemes,
                                    currentThemeId = savedTheme.id
                                )
                            )

                            val wasCreating = editingCustomTheme == null
                            val wasEditingActiveTheme =
                                editingCustomTheme?.id != null &&
                                        activeThemeSelection is ThemeSelection.Custom &&
                                        (activeThemeSelection as ThemeSelection.Custom).themeId == editingCustomTheme?.id

                            CalendarThemeStorage.upsertCustomTheme(
                                context = context,
                                theme = normalizedTheme
                            )

                            customThemesVersion++

                            if (wasCreating || wasEditingActiveTheme) {
                                val newSelection = ThemeSelection.Custom(normalizedTheme.id)

                                CalendarThemeStorage.saveThemeSelection(
                                    context = context,
                                    selection = newSelection
                                )

                                activeThemeSelection = newSelection
                                activeThemeConfig = normalizedTheme.themeConfig
                                CalendarSdk.updateTheme(normalizedTheme.themeConfig)
                            }

                            showPartialEditorSheet = false
                            editingCustomTheme = null
                        },
                        onRequestDismiss = { hasUnsavedChanges ->
                            if (hasUnsavedChanges) {
                                pendingEditorCloseAction = {
                                    showPartialEditorSheet = false
                                    editingCustomTheme = null
                                }
                                showDiscardChangesDialog = true
                            } else {
                                showPartialEditorSheet = false
                                editingCustomTheme = null
                            }
                        },
                    )
                }
            }

            if (showFullEditorSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        pendingEditorCloseAction = {
                            showFullEditorSheet = false
                            editingCustomTheme = null
                        }
                        showDiscardChangesDialog = true
                    },
                    sheetState = fullEditorSheetState,
                    containerColor = colors.surface
                ) {
                    FullThemeEditorScreen(
                        initialTheme = editingCustomTheme,
                        defaultBasePreset = activeThemeConfig.preset,
                        editorThemeConfig = activeThemeConfig,
                        onDismiss = {
                            showFullEditorSheet = false
                            editingCustomTheme = null
                        },
                        onRequestDismiss = { hasUnsavedChanges ->
                            if (hasUnsavedChanges) {
                                pendingEditorCloseAction = {
                                    showFullEditorSheet = false
                                    editingCustomTheme = null
                                }
                                showDiscardChangesDialog = true
                            } else {
                                showFullEditorSheet = false
                                editingCustomTheme = null
                            }
                        },
                        onApplyWithoutSave = { previewThemeConfig ->
                            activeThemeConfig = previewThemeConfig
                            CalendarSdk.updateTheme(previewThemeConfig)
                        },
                        onSave = { savedTheme ->
                            val normalizedTheme = savedTheme.copy(
                                name = makeUniqueThemeName(
                                    desiredName = savedTheme.name,
                                    existingThemes = customThemes,
                                    currentThemeId = savedTheme.id
                                )
                            )

                            CalendarThemeStorage.upsertCustomTheme(
                                context = context,
                                theme = normalizedTheme
                            )
                            CalendarThemeStorage.saveThemeSelection(
                                context = context,
                                selection = ThemeSelection.Custom(normalizedTheme.id)
                            )

                            customThemesVersion++
                            activeThemeSelection = ThemeSelection.Custom(normalizedTheme.id)
                            activeThemeConfig = normalizedTheme.themeConfig
                            CalendarSdk.updateTheme(normalizedTheme.themeConfig)

                            showFullEditorSheet = false
                            editingCustomTheme = null
                        },
                    )
                }
            }

            if (showDiscardChangesDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDiscardChangesDialog = false
                        pendingEditorCloseAction = null
                    },
                    title = {
                        androidx.compose.material3.Text("Закрыть редактор?")
                    },
                    text = {
                        androidx.compose.material3.Text("Есть несохранённые изменения. Закрыть редактор и потерять их?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDiscardChangesDialog = false
                                pendingEditorCloseAction?.invoke()
                                pendingEditorCloseAction = null
                            }
                        ) {
                            androidx.compose.material3.Text("Закрыть")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDiscardChangesDialog = false
                                pendingEditorCloseAction = null
                            }
                        ) {
                            androidx.compose.material3.Text("Остаться")
                        }
                    }
                )
            }
        }
    }
}

private fun resolveInitialThemeConfig(
    context: android.content.Context,
    fallbackThemeConfig: CalendarThemeConfig,
    selection: ThemeSelection
): CalendarThemeConfig {
    return when (selection) {
        is ThemeSelection.Preset -> {
            when (selection.preset) {
                CalendarThemePreset.Default -> CalendarThemeExamples.defaultTheme
                CalendarThemePreset.Ocean -> CalendarThemeExamples.oceanTheme
                CalendarThemePreset.Graphite -> CalendarThemeExamples.graphiteTheme
            }
        }

        is ThemeSelection.Custom -> {
            CalendarThemeStorage.findCustomThemeById(context, selection.themeId)?.themeConfig
                ?: fallbackThemeConfig
        }
    }
}

private fun makeUniqueThemeName(
    desiredName: String,
    existingThemes: List<CustomCalendarTheme>,
    currentThemeId: String?
): String {
    val baseName = desiredName.trim()
    if (baseName.isBlank()) return desiredName

    val existingNames = existingThemes
        .filterNot { it.id == currentThemeId }
        .map { it.name.trim().lowercase(Locale.ROOT) }
        .toSet()

    if (baseName.lowercase(Locale.ROOT) !in existingNames) {
        return baseName
    }

    var index = 2
    while ("$baseName ($index)".lowercase(Locale.ROOT) in existingNames) {
        index++
    }

    return "$baseName ($index)"
}