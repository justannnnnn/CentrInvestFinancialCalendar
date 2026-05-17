package com.example.sdk.presentation.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.sdk.domain.model.CalendarCategoryUi
import com.example.sdk.ui.theme.CalendarTheme

@Composable
fun AddTransactionCategorySection(
    categories: List<CalendarCategoryUi>,
    selectedCategory: CalendarCategoryUi?,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onCategorySelected: (CalendarCategoryUi) -> Unit
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
                categories = categories,
                selectedCategory = selectedCategory,
                onSelect = onCategorySelected
            )
        }
    }
}

@Composable
private fun CategorySelectorField(
    selectedCategory: CalendarCategoryUi?,
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
        CategoryIcon(
            iconUrl = selectedCategory?.iconUrl,
            color = selectedCategory?.color,
            fallback = "❓",
            size = 28
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = selectedCategory?.name ?: "Выберите категорию",
            style = typography.bodyLarge,
            color = if (selectedCategory == null) colors.textSecondary else colors.textPrimary,
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
    categories: List<CalendarCategoryUi>,
    selectedCategory: CalendarCategoryUi?,
    onSelect: (CalendarCategoryUi) -> Unit
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface, RoundedCornerShape(18.dp))
            .border(1.dp, colors.borderLight, RoundedCornerShape(18.dp))
    ) {
        categories.forEachIndexed { index, category ->
            val isSelected = category.id == selectedCategory?.id

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) colors.selectedBackground else Color.Transparent
                    )
                    .clickable { onSelect(category) }
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(
                    iconUrl = category.iconUrl,
                    color = category.color,
                    fallback = "❓",
                    size = 28
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = category.name,
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

            if (index != categories.lastIndex) {
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

@Composable
private fun CategoryIcon(
    iconUrl: String?,
    color: String?,
    fallback: String,
    size: Int
) {
    val colors = CalendarTheme.colors
    val typography = CalendarTheme.typography

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = color
                    ?.let { parseCategoryColor(it).copy(alpha = 0.12f) }
                    ?: colors.addSheetCategoryChip,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!iconUrl.isNullOrBlank() && iconUrl.startsWith("http")) {
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier.size((size - 6).dp),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = iconUrl?.takeIf { it.isNotBlank() } ?: fallback,
                style = typography.bodyMedium
            )
        }
    }
}

private fun parseCategoryColor(color: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(color))
    } catch (e: Exception) {
        Color(0xFF9CA3AF)
    }
}