package com.therxmv.churros.core.design.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.churrosColors

/**
 * Half-circle (speedometer / gauge) progress indicator with a centered percentage label.
 *
 * The arc spans 180° along the bottom half of the bounding circle, sweeping
 * from left (180°) to right (0°). The progress fill sweeps from left to right.
 *
 * @param progress value between 0f and 1f
 * @param size diameter of the full circle the arc is drawn on; the visible
 *             widget height is roughly size / 2 + strokeWidth / 2
 * @param strokeWidth thickness of the progress arc
 * @param label optional label rendered below the percentage text
 */
@Composable
fun ChurrosCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 10.dp,
    label: String? = null,
) {
    val trackColor = MaterialTheme.colorScheme.outline
    val progressColor = Honey500
    val percentText = "${(progress.coerceIn(0f, 1f) * 100).toInt()}%"
    val textSecondary = MaterialTheme.churrosColors.textSecondary
    val onBackground = MaterialTheme.colorScheme.onBackground

    // The widget is half a circle wide × (size/2) tall (plus stroke overhang).
    // We set the Box width = size and height = size/2 so no extra whitespace above.
    Box(
        modifier = modifier
            .width(size)
            .height(size / 2 + strokeWidth),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Canvas(
            modifier = Modifier
                .width(size)
                .height(size / 2 + strokeWidth),
        ) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = size.toPx() - strokeWidth.toPx()
            val topLeft = Offset(strokeWidth.toPx() / 2f, strokeWidth.toPx() / 2f)
            val arcSize = Size(diameter, diameter)

            // Track: full 180° arc from 180° (left) sweeping clockwise to 0° (right)
            drawArc(
                color = trackColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke,
            )

            // Progress fill: sweep from 180° clockwise proportionally
            val sweepAngle = 180f * progress.coerceIn(0f, 1f)
            if (sweepAngle > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = 180f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = stroke,
                )
            }
        }

        // Percentage + optional label sit at the bottom center, inside the arc opening
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = strokeWidth / 2),
        ) {
            Text(
                text = percentText,
                style = MaterialTheme.typography.titleLarge,
                color = onBackground,
            )
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary,
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun CircularProgressPreviewContent() {
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChurrosCircularProgress(progress = 0.75f, label = "done")
    }
}
