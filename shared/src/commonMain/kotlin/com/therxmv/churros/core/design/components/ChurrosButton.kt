package com.therxmv.churros.core.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import com.therxmv.churros.core.design.ChurrosIcons
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing

private val ButtonHeight = 56.dp

/**
 * Primary filled button for the Churros design system.
 *
 * Uses the brand Honey primary colour as background with white content
 * (per design rule #17: white text/icons on an orange background).
 * Corner shape is [ChurrosShapes.button] (full pill).
 *
 * @param text    Label displayed inside the button.
 * @param onClick Called when the button is tapped.
 * @param modifier Applied to the underlying [Button].
 * @param enabled When `false` the button is shown in a disabled visual state.
 */
@Composable
fun ChurrosButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
        )
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(ChurrosSpacing.XS))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

/**
 * Outlined secondary button for the Churros design system.
 *
 * Uses a 1dp Honey-coloured border with primary-coloured text.
 * Corner shape matches [ChurrosButton] for visual consistency.
 *
 * @param text    Label displayed inside the button.
 * @param onClick Called when the button is tapped.
 * @param modifier Applied to the underlying [OutlinedButton].
 * @param enabled When `false` the button is shown in a disabled visual state.
 */
@Composable
fun ChurrosOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

/**
 * Subtle text-only button for low-emphasis actions such as "Skip".
 *
 * Renders with [MaterialTheme.colorScheme.onBackground] label colour and no
 * background or border.
 *
 * @param text    Label displayed as the button text.
 * @param onClick Called when the button is tapped.
 * @param modifier Applied to the underlying [TextButton].
 */
@Composable
fun ChurrosTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ButtonPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosButton(
            text = "Create Account",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
        ChurrosOutlinedButton(
            text = "Sign In",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
        ChurrosTextButton(
            text = "Skip",
            onClick = {},
        )
    }
}
