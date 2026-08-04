package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.settings_section_preferences
import churros.shared.generated.resources.settings_title
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import org.jetbrains.compose.resources.stringResource

/**
 * Content card surface used for settings groups and feature sections.
 *
 * Uses [ChurrosShapes.card] for rounded corners and [MaterialTheme.colorScheme.surface]
 * as background. Inner content receives [ChurrosSpacing.cardPadding] on all sides.
 */
@Composable
fun ChurrosCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = ChurrosShapes.card,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.padding(ChurrosSpacing.cardPadding),
            content = content,
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun CardPreviewContent() {
    Column(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.settings_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(Res.string.settings_section_preferences),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.churrosColors.textSecondary,
                modifier = Modifier.padding(top = ChurrosSpacing.XS),
            )
        }
    }
}
