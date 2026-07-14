package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors

/**
 * A horizontal divider with an optional centred text label.
 *
 * When [label] is null, a plain full-width divider is rendered.
 * When [label] is provided, the line is split on both sides of the label text,
 * e.g. "——— or ———".
 */
@Composable
fun ChurrosDivider(
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    if (label == null) {
        HorizontalDivider(
            modifier = modifier,
            color = MaterialTheme.churrosColors.divider,
        )
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.churrosColors.divider,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.churrosColors.textTertiary,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.churrosColors.divider,
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun DividerPreviewContent() {
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
    ) {
        ChurrosDivider()
        ChurrosDivider(label = "or")
    }
}
