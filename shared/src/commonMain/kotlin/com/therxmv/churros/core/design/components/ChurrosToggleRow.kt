package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors

/**
 * Row with a label, optional description, and a trailing [Switch].
 *
 * Used on the Permissions screen and Settings screen.
 *
 * @param label          Primary label shown in bold body text.
 * @param checked        Current toggle state.
 * @param onCheckedChange Called when the user flips the toggle.
 * @param description    Optional secondary line below the label.
 */
@Composable
fun ChurrosToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = ChurrosSpacing.S),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = ChurrosSpacing.M),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.churrosColors.textSecondary,
                    modifier = Modifier.padding(top = ChurrosSpacing.XS),
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ToggleRowPreviewContent() {
    var approvalChecked by remember { mutableStateOf(false) }
    var assignChecked by remember { mutableStateOf(true) }
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosToggleRow(
            label = "Requires Parent Approval",
            description = "Kids must wait for review before getting points.",
            checked = approvalChecked,
            onCheckedChange = { approvalChecked = it },
        )
        ChurrosToggleRow(
            label = "Can Assign Own Chores",
            description = "Allow kids to suggest and start new tasks.",
            checked = assignChecked,
            onCheckedChange = { assignChecked = it },
        )
        ChurrosToggleRow(
            label = "Dark Mode",
            checked = false,
            onCheckedChange = {},
        )
    }
}
