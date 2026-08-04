package com.therxmv.churros.feature.chores.presentation.components

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
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.repeat_custom
import churros.shared.generated.resources.repeat_daily
import churros.shared.generated.resources.repeat_none
import churros.shared.generated.resources.repeat_weekly
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Repeat schedule options for a chore.
 * Mirrors the UI presentation — conversion to/from RRULE strings is handled
 * at the screen / ViewModel layer.
 */
enum class RepeatSchedule {
    NONE, DAILY, WEEKLY, CUSTOM;

    val labelRes: StringResource
        get() = when (this) {
            NONE -> Res.string.repeat_none
            DAILY -> Res.string.repeat_daily
            WEEKLY -> Res.string.repeat_weekly
            CUSTOM -> Res.string.repeat_custom
        }
}

/**
 * Repeat schedule pill-row selector.
 *
 * Renders one [RepeatSchedule] option per chip. The selected chip receives
 * the primary color; unselected chips use surfaceVariant.
 *
 * @param selected         Currently active schedule.
 * @param onSelected       Called when the user taps a chip.
 */
@Composable
fun ChurrosRepeatSelector(
    selected: RepeatSchedule,
    onSelected: (RepeatSchedule) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RepeatSchedule.entries.forEach { option ->
            val isSelected = option == selected
            val containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = isSelected,
                        onClick = { onSelected(option) },
                        role = Role.RadioButton,
                    ),
                shape = RoundedCornerShape(50),
                color = containerColor,
            ) {
                Text(
                    text = stringResource(option.labelRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
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
fun RepeatSelectorPreviewContent() {
    var schedule by remember { mutableStateOf(RepeatSchedule.WEEKLY) }
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosRepeatSelector(
            selected = schedule,
            onSelected = { schedule = it },
        )
    }
}
