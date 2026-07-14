package com.therxmv.churros.core.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Espresso
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.SemanticError
import com.therxmv.churros.core.design.churrosColors

private val ButtonMinHeight = 48.dp
private val ButtonContentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)

@Composable
fun ChurrosPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = Honey500,
            contentColor = Espresso,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.churrosColors.textDisabled,
        ),
        contentPadding = ButtonContentPadding,
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ChurrosSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: DrawableResource? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.churrosColors.textDisabled,
        ),
        contentPadding = ButtonContentPadding,
    ) {
        if (leadingIcon != null) {
            Image(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(ChurrosSpacing.S))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ChurrosTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.churrosColors.textDisabled,
        ),
        contentPadding = ButtonContentPadding,
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun ChurrosDangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ChurrosShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = SemanticError,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.churrosColors.textDisabled,
        ),
        contentPadding = ButtonContentPadding,
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ButtonsPreviewContent() {
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosPrimaryButton(text = "Primary", onClick = {})
        ChurrosSecondaryButton(text = "Secondary", onClick = {})
        ChurrosTextButton(text = "Text", onClick = {})
        ChurrosDangerButton(text = "Danger", onClick = {})
        ChurrosPrimaryButton(text = "Disabled", onClick = {}, enabled = false)
    }
}
