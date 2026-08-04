package com.therxmv.churros.core.design.components

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.chores_filter_all
import churros.shared.generated.resources.chores_filter_done
import churros.shared.generated.resources.chores_filter_today
import churros.shared.generated.resources.chores_filter_tomorrow
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.stringResource

/**
 * Selectable filter chip for date / assignee filter rows (All, Today, Tomorrow, Done, …).
 *
 * When [selected], background becomes [MaterialTheme.colorScheme.primary] with
 * [MaterialTheme.colorScheme.onPrimary] text. When unselected, uses the subtle
 * [MaterialTheme.colorScheme.surfaceVariant] container.
 */
@Composable
fun ChurrosFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick,
            role = Role.Tab,
        ),
        shape = RoundedCornerShape(50),
        color = containerColor,
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

@ChurrosPreview
@Composable
fun FilterChipPreviewContent() {
    var selected by remember { mutableIntStateOf(0) }
    val filters = listOf(
        stringResource(Res.string.chores_filter_all),
        stringResource(Res.string.chores_filter_today),
        stringResource(Res.string.chores_filter_tomorrow),
        stringResource(Res.string.chores_filter_done),
    )
    Row(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        filters.forEachIndexed { index, label ->
            ChurrosFilterChip(
                label = label,
                selected = selected == index,
                onClick = { selected = index },
            )
        }
    }
}
