package com.therxmv.churros.feature.chores.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import churros.shared.generated.resources.priority_high
import churros.shared.generated.resources.priority_low
import churros.shared.generated.resources.priority_medium
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val PriorityBorderWidth = 1.5.dp

/** Maps [ChorePriority] to its UI label resource. */
val ChorePriority.labelRes: StringResource
    get() = when (this) {
        ChorePriority.LOW -> Res.string.priority_low
        ChorePriority.MEDIUM -> Res.string.priority_medium
        ChorePriority.HIGH -> Res.string.priority_high
    }

/**
 * Three-option priority selector: Low / Medium / High.
 *
 * Renders the selected option with a primary-colored border and text; the others
 * are outlined with a subtle surface background (matching the Chores mockup).
 *
 * @param selected   Currently active [ChorePriority].
 * @param onSelected Called when the user picks a priority.
 */
@Composable
fun ChurrosPrioritySelector(
    selected: ChorePriority,
    onSelected: (ChorePriority) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChorePriority.entries.forEach { priority ->
            val isSelected = priority == selected
            val borderColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
            val textColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = PriorityBorderWidth,
                        color = borderColor,
                        shape = RoundedCornerShape(50),
                    )
                    .selectable(
                        selected = isSelected,
                        onClick = { onSelected(priority) },
                        role = Role.RadioButton,
                    ),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Text(
                    text = stringResource(priority.labelRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor,
                    modifier = Modifier.padding(
                        horizontal = ChurrosSpacing.S,
                        vertical = ChurrosSpacing.S,
                    ),
                    maxLines = 1,
                )
            }
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun PrioritySelectorPreviewContent() {
    var priority by remember { mutableStateOf(ChorePriority.MEDIUM) }
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosPrioritySelector(
            selected = priority,
            onSelected = { priority = it },
        )
    }
}
