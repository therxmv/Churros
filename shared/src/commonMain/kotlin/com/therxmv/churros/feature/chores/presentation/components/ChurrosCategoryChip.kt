package com.therxmv.churros.feature.chores.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chore_category_cleaning
import churros.shared.generated.resources.chore_category_custom
import churros.shared.generated.resources.chore_category_garden
import churros.shared.generated.resources.chore_category_kitchen
import churros.shared.generated.resources.chore_category_pets
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.stringResource

/**
 * Selectable chore category chip (icon-less label pill).
 *
 * When [selected] → primary container + onPrimary text.
 * When unselected → surfaceVariant + onSurfaceVariant text with primary outline.
 *
 * @param label    Category name shown on the chip.
 * @param selected Whether this chip is currently chosen.
 * @param onClick  Selection callback.
 */
@Composable
fun ChurrosCategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick,
            role = Role.RadioButton,
        ),
        shape = RoundedCornerShape(50),
        color = containerColor,
        border = if (!selected) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        } else null,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
            modifier = Modifier.padding(
                horizontal = ChurrosSpacing.M,
                vertical = ChurrosSpacing.S,
            ),
        )
    }
}

/**
 * Horizontally scrollable row of [ChurrosCategoryChip]s for chore category selection.
 *
 * Includes the default predefined categories plus a "+ Custom" option.
 *
 * @param selectedCategory  Currently selected category label (null = none).
 * @param onCategorySelected Called with the tapped category label.
 * @param categories         Ordered list of category labels; defaults to the standard set.
 */
@Composable
fun ChurrosCategoryPicker(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    categories: List<String> = defaultChoreCategories(),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        categories.forEach { category ->
            ChurrosCategoryChip(
                label = category,
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
            )
        }
    }
}

@Composable
fun defaultChoreCategories(): List<String> = listOf(
    stringResource(Res.string.chore_category_kitchen),
    stringResource(Res.string.chore_category_garden),
    stringResource(Res.string.chore_category_cleaning),
    stringResource(Res.string.chore_category_pets),
    stringResource(Res.string.chore_category_custom),
)

// ── Previews ────────────────────────────────────────────────────────────────────

@ChurrosPreview
@Composable
fun CategoryPickerPreviewContent() {
    var selected by remember { mutableStateOf<String?>("Kitchen") }
    val categories = listOf("Kitchen", "Garden", "Cleaning", "Pets", "+ Custom")
    Row(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        categories.forEach { cat ->
            ChurrosCategoryChip(
                label = cat,
                selected = selected == cat,
                onClick = { selected = cat },
            )
        }
    }
}
