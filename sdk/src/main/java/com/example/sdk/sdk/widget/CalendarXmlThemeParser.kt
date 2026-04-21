package com.example.sdk.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.sdk.R
import com.example.sdk.sdk.CalendarColorOverrides
import com.example.sdk.sdk.CalendarIconOverrides
import com.example.sdk.sdk.CalendarThemeConfig
import com.example.sdk.sdk.CalendarThemePreset
import com.example.sdk.sdk.CalendarTypographyOverrides

internal object CalendarXmlThemeParser {

    fun parse(
        context: Context,
        attrs: AttributeSet?,
        @StyleRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ): CalendarThemeConfig {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.FinancialCalendarWidget,
            defStyleAttr,
            defStyleRes
        )

        return try {
            val preset = when (
                typedArray.getInt(
                    R.styleable.FinancialCalendarWidget_calendarPreset,
                    0
                )
            ) {
                1 -> CalendarThemePreset.Ocean
                2 -> CalendarThemePreset.Graphite
                else -> CalendarThemePreset.Default
            }

            CalendarThemeConfig(
                preset = preset,
                colors = CalendarColorOverrides(
                    primary = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarPrimaryColor
                    ),
                    primaryVariant = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarPrimaryVariantColor
                    ),
                    background = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarBackgroundColor
                    ),
                    surface = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarSurfaceColor
                    ),
                    textPrimary = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarTextPrimaryColor
                    ),
                    textSecondary = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarTextSecondaryColor
                    ),
                    textTertiary = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarTextTertiaryColor
                    ),
                    borderLight = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarBorderLightColor
                    ),
                    borderMedium = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarBorderMediumColor
                    ),
                    selectedBackground = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarSelectedBackgroundColor
                    ),
                    selectedBorder = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarSelectedBorderColor
                    ),
                    income = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarIncomeColor
                    ),
                    incomeVariant = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarIncomeVariantColor
                    ),
                    expense = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarExpenseColor
                    ),
                    expenseVariant = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarExpenseVariantColor
                    ),
                    addSheetAccent = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarAddSheetAccentColor
                    ),
                    addSheetAccentSecondary = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarAddSheetAccentSecondaryColor
                    ),
                    addSheetCategoryChip = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarAddSheetCategoryChipColor
                    ),
                    addSheetRecurringAccent = typedArray.getColorOrNull(
                        R.styleable.FinancialCalendarWidget_calendarAddSheetRecurringAccentColor
                    )
                ),
                typography = CalendarTypographyOverrides(
                    titleLarge = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarTitleLargeTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarTitleLargeTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarTitleLargeLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarTitleLargeFontWeight
                    ),
                    titleMedium = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarTitleMediumTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarTitleMediumTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarTitleMediumLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarTitleMediumFontWeight
                    ),
                    bodyLarge = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarBodyLargeTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarBodyLargeTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarBodyLargeLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarBodyLargeFontWeight
                    ),
                    bodyMedium = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarBodyMediumTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarBodyMediumTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarBodyMediumLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarBodyMediumFontWeight
                    ),
                    bodySmall = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarBodySmallTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarBodySmallTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarBodySmallLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarBodySmallFontWeight
                    ),
                    labelMedium = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarLabelMediumTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarLabelMediumTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarLabelMediumLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarLabelMediumFontWeight
                    ),
                    labelSmall = typedArray.resolveTextStyle(
                        context = context,
                        textAppearanceIndex = R.styleable.FinancialCalendarWidget_calendarLabelSmallTextAppearance,
                        textSizeIndex = R.styleable.FinancialCalendarWidget_calendarLabelSmallTextSize,
                        lineHeightIndex = R.styleable.FinancialCalendarWidget_calendarLabelSmallLineHeight,
                        fontWeightIndex = R.styleable.FinancialCalendarWidget_calendarLabelSmallFontWeight
                    )
                ),
                icons = CalendarIconOverrides(
                    addIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarAddIcon
                    ),
                    monthTabIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarMonthTabIcon
                    ),
                    weekTabIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarWeekTabIcon
                    ),
                    dayTabIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarDayTabIcon
                    ),
                    calendarBottomNavIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarBottomNavIcon
                    ),
                    arrowLeftIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarArrowLeftIcon
                    ),
                    arrowRightIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarArrowRightIcon
                    ),
                    recurringIconRes = typedArray.getResourceIdOrNull(
                        R.styleable.FinancialCalendarWidget_calendarRecurringIcon
                    )
                )
            )
        } finally {
            typedArray.recycle()
        }
    }
}

private fun TypedArray.getColorOrNull(index: Int): Color? {
    return if (hasValue(index)) {
        Color(getColor(index, 0))
    } else {
        null
    }
}

private fun TypedArray.getResourceIdOrNull(index: Int): Int? {
    if (!hasValue(index)) return null
    val resId = getResourceId(index, 0)
    return resId.takeIf { it != 0 }
}

private fun TypedArray.resolveTextStyle(
    context: Context,
    textAppearanceIndex: Int,
    textSizeIndex: Int,
    lineHeightIndex: Int,
    fontWeightIndex: Int
): TextStyle? {
    val appearanceResId = getResourceIdOrNull(textAppearanceIndex)
    val appearanceStyle = appearanceResId?.let { resolveTextAppearance(context, it) } ?: TextStyle.Default

    val explicitFontSize = getDimensionSpOrNull(textSizeIndex)
    val explicitLineHeight = getDimensionSpOrNull(lineHeightIndex)
    val explicitFontWeight = getFontWeightOrNull(fontWeightIndex)

    val merged = appearanceStyle.merge(
        TextStyle(
            fontSize = explicitFontSize ?: TextUnit.Unspecified,
            lineHeight = explicitLineHeight ?: TextUnit.Unspecified,
            fontWeight = explicitFontWeight
        )
    )

    return merged.takeIfMeaningful()
}

private fun resolveTextAppearance(
    context: Context,
    @StyleRes textAppearanceResId: Int
): TextStyle {
    val ta = context.obtainStyledAttributes(
        textAppearanceResId,
        intArrayOf(
            android.R.attr.textSize,
            android.R.attr.lineHeight,
            android.R.attr.textFontWeight,
            android.R.attr.textStyle
        )
    )

    return try {
        val fontSize = if (ta.hasValue(0)) pxToSp(context, ta.getDimension(0, 0f)) else null
        val lineHeight = if (ta.hasValue(1)) pxToSp(context, ta.getDimension(1, 0f)) else null

        val directFontWeight = if (ta.hasValue(2)) ta.getInt(2, 400) else null
        val fallbackTextStyle = if (ta.hasValue(3)) ta.getInt(3, 0) else null

        val resolvedWeight = when {
            directFontWeight != null -> FontWeight(directFontWeight)
            fallbackTextStyle != null && (fallbackTextStyle and 1) != 0 -> FontWeight.Bold
            else -> null
        }

        TextStyle(
            fontSize = fontSize ?: TextUnit.Unspecified,
            lineHeight = lineHeight ?: TextUnit.Unspecified,
            fontWeight = resolvedWeight
        )
    } finally {
        ta.recycle()
    }
}

private fun TypedArray.getDimensionSpOrNull(index: Int): TextUnit? {
    if (!hasValue(index)) return null
    val px = getDimension(index, 0f)
    if (px <= 0f) return null
    return pxToSpFromSystem(px)
}

private fun TypedArray.getFontWeightOrNull(index: Int): FontWeight? {
    if (!hasValue(index)) return null
    val value = getInt(index, 400)
    return FontWeight(value)
}

private fun pxToSp(context: Context, px: Float): TextUnit {
    val scaledDensity = context.resources.displayMetrics.scaledDensity
    return (px / scaledDensity).sp
}

private fun pxToSpFromSystem(px: Float): TextUnit {
    val scaledDensity = android.content.res.Resources.getSystem().displayMetrics.scaledDensity
    return (px / scaledDensity).sp
}

private fun TextStyle.takeIfMeaningful(): TextStyle? {
    val hasFontSize = fontSize != TextUnit.Unspecified
    val hasLineHeight = lineHeight != TextUnit.Unspecified
    val hasFontWeight = fontWeight != null

    return if (hasFontSize || hasLineHeight || hasFontWeight) this else null
}