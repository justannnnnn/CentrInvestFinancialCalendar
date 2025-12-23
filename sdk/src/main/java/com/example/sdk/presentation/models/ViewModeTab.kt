package com.example.sdk.presentation.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.sdk.R

sealed class ViewModeTab(
    @StringRes val name: Int,
    @DrawableRes val icon: Int
) {
    data object Month: ViewModeTab(
        name = R.string.month,
        icon = R.drawable.ic_month
    )

    data object Week: ViewModeTab(
        name = R.string.week,
        icon = R.drawable.ic_week
    )

    data object Day: ViewModeTab(
        name = R.string.day,
        icon = R.drawable.ic_day
    )
}