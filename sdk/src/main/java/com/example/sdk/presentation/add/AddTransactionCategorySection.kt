package com.example.sdk.presentation.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sdk.domain.model.Category
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun AddTransactionCategorySection(
    selectedCategory: Category,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onCategorySelected: (Category) -> Unit
) {
    SectionLabel(text = "Категория")

    Spacer(modifier = Modifier.height(8.dp))

    CategorySelectorField(
        selectedCategory = selectedCategory,
        expanded = expanded,
        onToggle = onToggleExpanded
    )

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))

            CategoryDropdownCard(
                selectedCategory = selectedCategory,
                onSelect = onCategorySelected
            )
        }
    }
}

@Composable
private fun CategorySelectorField(
    selectedCategory: Category,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(18.dp))
            .border(1.dp, colors.borderLight, RoundedCornerShape(18.dp))
            .clickable { onToggle() }
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(colors.addSheetCategoryChip, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedCategory.icon,
                style = typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = selectedCategory.title,
            style = typography.bodyLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (expanded) "⌃" else "⌄",
            style = typography.bodyLarge,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun CategoryDropdownCard(
    selectedCategory: Category,
    onSelect: (Category) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(18.dp))
            .border(1.dp, colors.borderLight, RoundedCornerShape(18.dp))
    ) {
        AddTransactionConstants.categories.forEachIndexed { index, item ->
            val isSelected = item.category == selectedCategory

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) colors.selectedBackground else Color.Transparent
                    )
                    .clickable { onSelect(item.category) }
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            colors.addSheetCategoryChip,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.category.icon,
                        style = typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = item.category.title,
                    style = typography.bodyLarge,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                if (isSelected) {
                    Text(
                        text = "✓",
                        style = typography.bodyLarge,
                        color = colors.primary
                    )
                }
            }

            if (index != AddTransactionConstants.categories.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colors.borderLight)
                )
            }
        }
    }
}