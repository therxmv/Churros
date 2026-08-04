package com.therxmv.churros.core.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.progress_ring_content_desc
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.stringResource

/**
 * Circular progress indicator for family/personal goal progress.
 *
 * Draws a rounded track arc and a progress arc on top. Optionally renders
 * the percentage label inside the ring.
 *
 * @param progress     Fraction in `[0f..1f]`.
 * @param size         Overall diameter of the ring in dp.
 * @param strokeWidth  Width of the progress arc stroke in dp.
 * @param showLabel    When true, renders `"85%"` text in the centre.
 */
@Composable
fun ChurrosProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    strokeWidth: Dp = 6.dp,
    showLabel: Boolean = true,
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val clampedProgress = progress.coerceIn(0f, 1f)
    val percentage = (clampedProgress * 100).toInt()
    val contentDesc = stringResource(Res.string.progress_ring_content_desc, percentage)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .semantics { contentDescription = contentDesc },
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val inset = strokePx / 2f
            val arcSize = Size(
                width = this.size.width - strokePx,
                height = this.size.height - strokePx,
            )
            val arcOffset = Offset(x = inset, y = inset)

            // Background track — full circle
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
                topLeft = arcOffset,
                size = arcSize,
            )
            // Progress arc
            if (clampedProgress > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * clampedProgress,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    topLeft = arcOffset,
                    size = arcSize,
                )
            }
        }
        if (showLabel) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ProgressRingPreviewContent() {
    Row(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.M),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChurrosProgressRing(progress = 0.85f, size = 80.dp)
        ChurrosProgressRing(progress = 0.50f, size = 64.dp)
        ChurrosProgressRing(progress = 0.25f, size = 48.dp, strokeWidth = 4.dp)
        ChurrosProgressRing(progress = 1.00f, size = 48.dp, strokeWidth = 4.dp)
    }
}
