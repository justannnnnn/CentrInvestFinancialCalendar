package com.example.sdk.sdk

/**
 * Примеры интеграции XML-виджета календаря в host app.
 *
 * Основной сценарий для host app:
 * 1) Подключить SDK как зависимость.
 * 2) Вставить FinancialCalendarWidget в XML layout.
 * 3) Передать цвета/иконки через app:-атрибуты, style или theme overlay.
 *
 * Важно:
 * сам SDK внутри рендерится на Compose,
 * но host app может использовать его как обычный Android View.
 */
object CalendarWidgetUsageExamples {

    /*
    ============================================================
    1. Подключение зависимости
    ============================================================

    implementation("your.group:calendar-sdk:version")
    */

    /*
    ============================================================
    2. Пример вставки виджета в XML layout
    ============================================================

    <com.example.sdk.widget.FinancialCalendarWidget
        android:id="@+id/financialCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:calendarPreset="default"
        app:calendarPrimaryColor="@color/bank_primary"
        app:calendarBackgroundColor="@android:color/white"
        app:calendarSurfaceColor="@android:color/white"
        app:calendarTextPrimaryColor="@color/bank_text_primary"
        app:calendarTextSecondaryColor="@color/bank_text_secondary"
        app:calendarBorderLightColor="@color/bank_border_light"
        app:calendarMonthTabIcon="@drawable/ic_bank_month"
        app:calendarWeekTabIcon="@drawable/ic_bank_week"
        app:calendarDayTabIcon="@drawable/ic_bank_day" />
    */

    /*
    ============================================================
    3. Пример host style
    ============================================================

    <style name="Widget.Bank.FinancialCalendar"
        parent="Widget.CentrInvest.FinancialCalendar">
        <item name="calendarPreset">default</item>
        <item name="calendarPrimaryColor">@color/bank_primary</item>
        <item name="calendarBackgroundColor">@android:color/white</item>
        <item name="calendarSurfaceColor">@android:color/white</item>
        <item name="calendarTextPrimaryColor">@color/bank_text_primary</item>
        <item name="calendarTextSecondaryColor">@color/bank_text_secondary</item>
        <item name="calendarMonthTabIcon">@drawable/ic_bank_month</item>
        <item name="calendarWeekTabIcon">@drawable/ic_bank_week</item>
        <item name="calendarDayTabIcon">@drawable/ic_bank_day</item>
    </style>

    <style name="Theme.BankApp" parent="Theme.Material3.Light.NoActionBar">
        <item name="financialCalendarStyle">@style/Widget.Bank.FinancialCalendar</item>
    </style>
    */

    /*
    ============================================================
    4. Пример theme overlay
    ============================================================

    <style name="ThemeOverlay.Bank.Calendar">
        <item name="calendarPrimaryColor">@color/bank_primary</item>
        <item name="calendarTextPrimaryColor">@color/bank_text_primary</item>
        <item name="calendarSurfaceColor">@android:color/white</item>
    </style>

    <com.example.sdk.widget.FinancialCalendarWidget
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:calendarThemeOverlay="@style/ThemeOverlay.Bank.Calendar" />
    */
}