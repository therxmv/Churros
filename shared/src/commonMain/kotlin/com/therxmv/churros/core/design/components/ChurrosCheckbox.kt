package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.churrosColors

@Composable
fun ChurrosCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = Honey500,
            checkmarkColor = MaterialTheme.colorScheme.onPrimary,
            uncheckedColor = MaterialTheme.colorScheme.outline,
            disabledCheckedColor = MaterialTheme.churrosColors.textDisabled,
            disabledUncheckedColor = MaterialTheme.churrosColors.textDisabled,
        ),
    )
}

@Composable
fun ChurrosCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.XS),
    ) {
        ChurrosCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
            ),
            color = if (checked) {
                MaterialTheme.churrosColors.textTertiary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun CheckboxPreviewContent() {
    var checked1 by remember { mutableStateOf(false) }
    var checked2 by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosCheckboxRow(
            label = "Take out trash",
            checked = checked1,
            onCheckedChange = { checked1 = it },
        )
        ChurrosCheckboxRow(
            label = "Vacuum living room",
            checked = checked2,
            onCheckedChange = { checked2 = it },
        )
    }
}
