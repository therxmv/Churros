package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.Honey500
import com.therxmv.churros.core.design.SemanticError
import com.therxmv.churros.core.design.churrosColors

@Composable
fun ChurrosTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation =
        androidx.compose.ui.text.input.VisualTransformation.None,
) {
    val isError = errorMessage != null

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
            placeholder = placeholder?.let {
                { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            isError = isError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            singleLine = true,
            shape = ChurrosShapes.button,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Honey500,
                focusedLabelColor = Honey500,
                cursorColor = Honey500,
                errorBorderColor = SemanticError,
                errorLabelColor = SemanticError,
                errorCursorColor = SemanticError,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.churrosColors.textDisabled,
                disabledTextColor = MaterialTheme.churrosColors.textDisabled,
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = SemanticError,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = ChurrosSpacing.M, top = ChurrosSpacing.XS),
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
private fun TextFieldPreviewContent() {
    var value by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(ChurrosSpacing.M),
        verticalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        ChurrosTextField(
            value = value,
            onValueChange = { value = it },
            label = "Email",
        )
        ChurrosTextField(
            value = "wrong@",
            onValueChange = {},
            label = "Email",
            errorMessage = "Enter a valid email address",
        )
        ChurrosTextField(
            value = "",
            onValueChange = {},
            label = "Password",
            enabled = false,
        )
    }
}
