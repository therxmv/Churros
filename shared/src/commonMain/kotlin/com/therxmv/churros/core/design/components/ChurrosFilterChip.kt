package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Espresso
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.churrosColors

/**
 * Churros-styled filter chip.
 *
 * Selected state: Honey500 container with Espresso text.
 * Unselected state: surfaceVariant container with textSecondary text.
 */
@Composable
fun ChurrosFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        modifier = modifier,
        shape = ChurrosShapes.button,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.churrosColors.textSecondary,
            selectedContainerColor = Honey500,
            selectedLabelColor = Espresso,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = Honey500,
        ),
    )
}

// ── Preview ───────────────────────────────────────────────────────────────────

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun FilterChipPreviewContent() {
    Row(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosFilterChip(label = "Today", selected = true, onClick = {})
        ChurrosFilterChip(label = "This Week", selected = false, onClick = {})
        ChurrosFilterChip(label = "All", selected = false, onClick = {})
    }
}
