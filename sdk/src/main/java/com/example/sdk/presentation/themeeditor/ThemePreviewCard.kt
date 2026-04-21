package com.example.sdk.presentation.themeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sdk.presentation.add.AddTransactionPreviewCard
import com.example.sdk.presentation.components.IconWrapper
import com.example.sdk.ui.theme.CalendarTheme
import com.example.sdk.ui.theme.FinancialCalendarTheme

@Composable
fun ThemePreviewCard(
    draft: ThemeEditorDraft,
    modifier: Modifier = Modifier
) {
    FinancialCalendarTheme(themeConfig = draft.toThemeConfig()) {
        val colors = CalendarTheme.colors
        val typography = CalendarTheme.typography
        val icons = CalendarTheme.icons

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(colors.background, RoundedCornerShape(22.dp))
                .border(
                    width = 1.dp,
                    color = colors.borderLight,
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(14.dp)
        ) {
            PreviewHeader()

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PreviewTabChip(
                    text = "Month",
                    iconRes = icons.monthTab,
                    isSelected = true
                )
                PreviewTabChip(
                    text = "Week",
                    iconRes = icons.weekTab,
                    isSelected = false
                )
                PreviewTabChip(
                    text = "Day",
                    iconRes = icons.dayTab,
                    isSelected = false
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PreviewMetricCard(
                    title = "Доходы",
                    amount = "+12 500 ₽",
                    accentColor = colors.income,
                    modifier = Modifier.weight(1f)
                )

                PreviewMetricCard(
                    title = "Расходы",
                    amount = "-3 250 ₽",
                    accentColor = colors.expense,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PreviewCalendarRow(
                    days = listOf("10", "11", "12", "13", "14", "15", "16"),
                    selectedIndex = 2
                )

                PreviewCalendarRow(
                    days = listOf("17", "18", "19", "20", "21", "22", "23"),
                    selectedIndex = null
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            PreviewTransactionCard()

            Spacer(modifier = Modifier.height(14.dp))

            PreviewBottomNav()
        }
    }
}

@Composable
private fun PreviewHeader() {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography
    val icons = CalendarTheme.icons

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(colors.surface, RoundedCornerShape(12.dp))
                .border(1.dp, colors.borderLight, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = icons.arrowLeft,
                color = colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Апрель 2026",
                style = typography.titleMedium,
                color = colors.textPrimary
            )

            Text(
                text = "Preview draft theme",
                style = typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(colors.primary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = icons.add,
                color = colors.surface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun PreviewTransactionCard() {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = "15 апреля",
            style = typography.bodyLarge,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Оплата подписки",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "-499 ₽",
            style = typography.titleMedium,
            color = colors.expense
        )

        Spacer(modifier = Modifier.height(14.dp))

        AddTransactionPreviewCard()

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun PreviewBottomNav() {
    val colors = CalendarTheme.colors
    val icons = CalendarTheme.icons

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviewBottomNavItem(
            label = "Calendar",
            iconRes = icons.calendarBottomNav,
            isSelected = true
        )
        PreviewBottomNavItem(
            label = "Stats",
            iconRes = icons.weekTab,
            isSelected = false
        )
        PreviewBottomNavItem(
            label = "More",
            iconRes = icons.dayTab,
            isSelected = false
        )
    }
}

@Composable
private fun PreviewTabChip(
    text: String,
    iconRes: Int,
    isSelected: Boolean
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .background(
                if (isSelected) colors.selectedBackground else colors.surface,
                RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) colors.selectedBorder else colors.borderLight,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconWrapper(
            iconRes = iconRes,
            color = colors.textPrimary,
            modifier = Modifier.size(14.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            style = typography.bodySmall,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun PreviewMetricCard(
    title: String,
    amount: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = modifier
            .background(colors.surface, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = colors.borderLight,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 24.dp, height = 4.dp)
                .background(accentColor, RoundedCornerShape(100.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            style = typography.bodySmall,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = amount,
            style = typography.titleMedium,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun PreviewCalendarRow(
    days: List<String>,
    selectedIndex: Int?
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        days.forEachIndexed { index, day ->
            val isSelected = selectedIndex == index

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .background(
                        if (isSelected) colors.selectedBackground else colors.surface,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) colors.selectedBorder else colors.borderLight,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = typography.bodySmall,
                    color = colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun PreviewBottomNavItem(
    label: String,
    iconRes: Int,
    isSelected: Boolean
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    if (isSelected) colors.selectedBackground else colors.surface,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            IconWrapper(
                iconRes = iconRes,
                color = if (isSelected) colors.primary else colors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = typography.bodySmall,
            color = if (isSelected) colors.textPrimary else colors.textSecondary
        )
    }
}