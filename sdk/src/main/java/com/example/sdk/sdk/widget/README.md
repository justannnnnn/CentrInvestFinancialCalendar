```markdown
# CentrInvest Financial Calendar SDK

Android SDK-библиотека финансового календаря на Jetpack Compose, предназначенная для встраивания в host application как обычный Android View.

## 📋 Содержание

- [Что умеет библиотека](#что-умеет-библиотека)
- [Как это работает](#как-это-работает)
- [Подключение SDK](#подключение-sdk)
- [Основной способ использования](#основной-способ-использования)
- [Передача темы через XML](#передача-темы-через-xml)
  - [Пример с цветами и иконками](#пример-с-цветами-и-иконками)
  - [Передача typography через XML](#передача-typography-через-xml)
  - [Использование style](#использование-style)
  - [Использование через theme attribute](#использование-через-theme-attribute)
  - [Использование theme overlay](#использование-theme-overlay)
- [Как банковскому приложению вызвать библиотеку](#как-банковскому-приложению-вызвать-библиотеку)
- [Публичные XML-атрибуты](#публичные-xml-атрибуты)
- [Важные замечания](#важные-замечания)
- [Минимальный рабочий пример](#минимальный-рабочий-пример)

---

## Что умеет библиотека

Библиотека позволяет встроить финансовый календарь в приложение банка и передавать в него настройки оформления через XML:

- ✅ preset темы
- ✅ цвета
- ✅ иконки
- ✅ typography
- ✅ theme overlay

Внутри библиотека реализована на Compose, но для host app используется обычный Android widget:

```java
com.example.sdk.widget.FinancialCalendarWidget
```

Это позволяет подключать SDK в XML layout без необходимости вызывать Compose-обёртку в банковском приложении.

---

## Как это работает

Host app передаёт параметры через XML / style / theme overlay.

Дальше внутри SDK происходит цепочка:

1. `FinancialCalendarWidget` создаётся в layout
2. Widget читает XML-атрибуты через `obtainStyledAttributes(...)`
3. `CalendarXmlThemeParser` преобразует XML в `CalendarThemeConfig`
4. `FinancialCalendarView(themeConfig = ...)` получает эту конфигурацию
5. `FinancialCalendarTheme(...)` применяет:
    - colors
    - typography
    - icons

> **Важно:** XML никуда не отправляется. Она читается локально в процессе создания виджета.

---

## Подключение SDK

Пример подключения библиотеки как зависимости:

```gradle
implementation("your.group:calendar-sdk:version")
```

Если библиотека подключена как module dependency внутри проекта:

```gradle
implementation(project(":sdk"))
```

---

## Основной способ использования

Host app вставляет widget в XML layout:

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:id="@+id/financialCalendar"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Это минимальный вариант без кастомных параметров.

---

## Передача темы через XML

### Пример с цветами и иконками

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:id="@+id/financialCalendar"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:calendarPreset="preset_default"
    app:calendarPrimaryColor="@color/bank_primary"
    app:calendarBackgroundColor="@android:color/white"
    app:calendarSurfaceColor="@android:color/white"
    app:calendarTextPrimaryColor="@color/bank_text_primary"
    app:calendarTextSecondaryColor="@color/bank_text_secondary"
    app:calendarBorderLightColor="@color/bank_border_light"
    app:calendarMonthTabIcon="@drawable/ic_bank_month"
    app:calendarWeekTabIcon="@drawable/ic_bank_week"
    app:calendarDayTabIcon="@drawable/ic_bank_day" />
```

### Передача typography через XML

#### Вариант 1. Через TextAppearance

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:calendarTitleLargeTextAppearance="@style/TextAppearance.Bank.Calendar.TitleLarge"
    app:calendarBodyMediumTextAppearance="@style/TextAppearance.Bank.Calendar.BodyMedium"
    app:calendarLabelSmallTextAppearance="@style/TextAppearance.Bank.Calendar.LabelSmall" />
```

Пример text appearance в host app:

```xml
<style name="TextAppearance.Bank.Calendar.TitleLarge">
    <item name="android:textSize">24sp</item>
    <item name="android:lineHeight">28sp</item>
    <item name="android:textStyle">bold</item>
</style>

<style name="TextAppearance.Bank.Calendar.BodyMedium">
    <item name="android:textSize">14sp</item>
    <item name="android:lineHeight">20sp</item>
    <item name="android:textStyle">normal</item>
</style>

<style name="TextAppearance.Bank.Calendar.LabelSmall">
    <item name="android:textSize">12sp</item>
    <item name="android:lineHeight">16sp</item>
</style>
```

#### Вариант 2. Через явные XML-атрибуты

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:calendarTitleMediumTextSize="20sp"
    app:calendarTitleMediumLineHeight="24sp"
    app:calendarTitleMediumFontWeight="700"
    app:calendarLabelSmallTextSize="12sp"
    app:calendarLabelSmallLineHeight="16sp"
    app:calendarLabelSmallFontWeight="500" />
```

### Использование style

Host app может описать стиль для календаря:

```xml
<style name="Widget.Bank.FinancialCalendar"
    parent="Widget.CentrInvest.FinancialCalendar">

    <item name="calendarPreset">preset_default</item>

    <item name="calendarPrimaryColor">@color/bank_primary</item>
    <item name="calendarBackgroundColor">@android:color/white</item>
    <item name="calendarSurfaceColor">@android:color/white</item>

    <item name="calendarTextPrimaryColor">@color/bank_text_primary</item>
    <item name="calendarTextSecondaryColor">@color/bank_text_secondary</item>

    <item name="calendarBorderLightColor">@color/bank_border_light</item>

    <item name="calendarMonthTabIcon">@drawable/ic_bank_month</item>
    <item name="calendarWeekTabIcon">@drawable/ic_bank_week</item>
    <item name="calendarDayTabIcon">@drawable/ic_bank_day</item>

    <item name="calendarTitleLargeTextAppearance">@style/TextAppearance.Bank.Calendar.TitleLarge</item>
    <item name="calendarBodyMediumTextAppearance">@style/TextAppearance.Bank.Calendar.BodyMedium</item>
    <item name="calendarLabelSmallTextAppearance">@style/TextAppearance.Bank.Calendar.LabelSmall</item>
</style>
```

И потом использовать этот стиль в layout:

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    style="@style/Widget.Bank.FinancialCalendar"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

### Использование через theme attribute

Библиотека поддерживает theme attribute:

```
financialCalendarStyle
```

Host app может назначить стиль глобально в своей теме:

```xml
<style name="Theme.BankApp" parent="Theme.Material3.Light.NoActionBar">
    <item name="financialCalendarStyle">@style/Widget.Bank.FinancialCalendar</item>
</style>
```

После этого `FinancialCalendarWidget` будет использовать этот стиль по умолчанию.

### Использование theme overlay

Host app может передать локальный overlay только для одного экземпляра виджета:

```xml
<style name="ThemeOverlay.Bank.Calendar">
    <item name="calendarPrimaryColor">@color/bank_primary</item>
    <item name="calendarTextPrimaryColor">@color/bank_text_primary</item>
    <item name="calendarSurfaceColor">@android:color/white</item>
</style>
```

Использование:

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:calendarThemeOverlay="@style/ThemeOverlay.Bank.Calendar" />
```

---

## Как банковскому приложению вызвать библиотеку

Есть два основных сценария.

### Сценарий 1. Вызов через XML layout

Host app создаёт экран, например `activity_calendar.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.example.sdk.widget.FinancialCalendarWidget
        android:id="@+id/financialCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        style="@style/Widget.Bank.FinancialCalendar" />

</FrameLayout>
```

После этого activity или fragment просто открывает этот layout как обычный экран.

### Сценарий 2. Программная установка конфигурации

Если host app хочет сначала создать widget, а потом передать тему из кода:

```kotlin
val calendarWidget = findViewById<FinancialCalendarWidget>(R.id.financialCalendar)

calendarWidget.setThemeConfig(
    CalendarThemeConfig(
        preset = CalendarThemePreset.Default,
        colors = CalendarColorOverrides(
            primary = Color(0xFF00A86B)
        )
    )
)
```

Этот способ поддерживается, но для интеграции в host app предпочтителен XML.

---

## Публичные XML-атрибуты

### Preset

| Атрибут | Значения |
|---------|----------|
| `app:calendarPreset` | `preset_default`<br>`ocean`<br>`graphite` |

### Colors

| Атрибут |
|---------|
| `app:calendarPrimaryColor` |
| `app:calendarPrimaryVariantColor` |
| `app:calendarBackgroundColor` |
| `app:calendarSurfaceColor` |
| `app:calendarTextPrimaryColor` |
| `app:calendarTextSecondaryColor` |
| `app:calendarTextTertiaryColor` |
| `app:calendarBorderLightColor` |
| `app:calendarBorderMediumColor` |
| `app:calendarSelectedBackgroundColor` |
| `app:calendarSelectedBorderColor` |
| `app:calendarIncomeColor` |
| `app:calendarIncomeVariantColor` |
| `app:calendarExpenseColor` |
| `app:calendarExpenseVariantColor` |
| `app:calendarAddSheetAccentColor` |
| `app:calendarAddSheetAccentSecondaryColor` |
| `app:calendarAddSheetCategoryChipColor` |
| `app:calendarAddSheetRecurringAccentColor` |

### Icons

| Атрибут |
|---------|
| `app:calendarAddIcon` |
| `app:calendarMonthTabIcon` |
| `app:calendarWeekTabIcon` |
| `app:calendarDayTabIcon` |
| `app:calendarBottomNavIcon` |
| `app:calendarArrowLeftIcon` |
| `app:calendarArrowRightIcon` |
| `app:calendarRecurringIcon` |

### Typography

| TextAppearance | Явные атрибуты |
|----------------|----------------|
| `app:calendarTitleLargeTextAppearance` | `app:calendarTitleLargeTextSize`<br>`app:calendarTitleLargeLineHeight`<br>`app:calendarTitleLargeFontWeight` |
| `app:calendarTitleMediumTextAppearance` | `app:calendarTitleMediumTextSize`<br>`app:calendarTitleMediumLineHeight`<br>`app:calendarTitleMediumFontWeight` |
| `app:calendarBodyLargeTextAppearance` | `app:calendarBodyLargeTextSize`<br>`app:calendarBodyLargeLineHeight`<br>`app:calendarBodyLargeFontWeight` |
| `app:calendarBodyMediumTextAppearance` | `app:calendarBodyMediumTextSize`<br>`app:calendarBodyMediumLineHeight`<br>`app:calendarBodyMediumFontWeight` |
| `app:calendarBodySmallTextAppearance` | `app:calendarBodySmallTextSize`<br>`app:calendarBodySmallLineHeight`<br>`app:calendarBodySmallFontWeight` |
| `app:calendarLabelMediumTextAppearance` | `app:calendarLabelMediumTextSize`<br>`app:calendarLabelMediumLineHeight`<br>`app:calendarLabelMediumFontWeight` |
| `app:calendarLabelSmallTextAppearance` | `app:calendarLabelSmallTextSize`<br>`app:calendarLabelSmallLineHeight`<br>`app:calendarLabelSmallFontWeight` |

### Overlay

| Атрибут |
|---------|
| `app:calendarThemeOverlay` |

---

## Важные замечания

- 📌 Библиотека предназначена для встраивания в host app как Android widget.
- 📌 XML не передаётся по сети и не отправляется наружу.
- 📌 XML используется только как источник конфигурации при создании widget.
- 📌 Внутри SDK UI построен на Compose, но host app работает с ним как с обычным View.
- 📌 Для корректной работы typography UI-компоненты SDK должны использовать `CalendarTheme.typography`, а не захардкоженные fontSize / fontWeight.

---

## Минимальный рабочий пример

### Host app theme

```xml
<style name="Theme.BankApp" parent="Theme.Material3.Light.NoActionBar">
    <item name="financialCalendarStyle">@style/Widget.Bank.FinancialCalendar</item>
</style>
```

### Host app calendar style

```xml
<style name="Widget.Bank.FinancialCalendar"
    parent="Widget.CentrInvest.FinancialCalendar">
    <item name="calendarPreset">preset_default</item>
    <item name="calendarPrimaryColor">@color/bank_primary</item>
    <item name="calendarTextPrimaryColor">@color/bank_text_primary</item>
    <item name="calendarTitleLargeTextAppearance">@style/TextAppearance.Bank.Calendar.TitleLarge</item>
</style>
```

### Host app layout

```xml
<com.example.sdk.widget.FinancialCalendarWidget
    android:id="@+id/financialCalendar"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

После этого экран будет использовать тему, переданную из host app в SDK через XML.
```