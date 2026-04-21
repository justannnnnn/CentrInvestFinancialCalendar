package com.example.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.sdk.R
import com.example.sdk.presentation.FinancialCalendarView
import com.example.sdk.sdk.CalendarThemeConfig
import com.google.android.material.theme.overlay.MaterialThemeOverlay

class FinancialCalendarWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.financialCalendarStyle
) : FrameLayout(
    MaterialThemeOverlay.wrap(
        context,
        attrs,
        defStyleAttr,
        R.style.Widget_CentrInvest_FinancialCalendar,
        intArrayOf(R.attr.calendarThemeOverlay)
    ),
    attrs,
    defStyleAttr
) {

    private val composeView: ComposeView
    private var themeConfig: CalendarThemeConfig

    init {
        val themedContext = getContext()

        themeConfig = CalendarXmlThemeParser.parse(
            context = themedContext,
            attrs = attrs,
            defStyleAttr = defStyleAttr,
            defStyleRes = R.style.Widget_CentrInvest_FinancialCalendar
        )

        composeView = ComposeView(themedContext).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindow
            )
        }

        addView(composeView)
        render()
    }

    fun setThemeConfig(newThemeConfig: CalendarThemeConfig) {
        themeConfig = newThemeConfig
        render()
    }

    fun getThemeConfig(): CalendarThemeConfig = themeConfig

    private fun render() {
        composeView.setContent {
            FinancialCalendarView(
                themeConfig = themeConfig
            )
        }
    }
}