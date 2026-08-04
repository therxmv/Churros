package com.therxmv.churros.core.design.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.otp_field_content_desc
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosShapes
import com.therxmv.churros.core.design.ChurrosSpacing
import com.therxmv.churros.core.design.churrosColors
import org.jetbrains.compose.resources.stringResource

private val OtpDigitSize = 48.dp
private val OtpBorderWidth = 1.5.dp

/**
 * 6-digit OTP input field for the Verify Email screen.
 *
 * Renders [digitCount] individual rounded boxes. A single hidden [BasicTextField]
 * captures all input; each box reflects one character of the underlying value.
 *
 * @param value        Current OTP string (digits only, length ≤ [digitCount]).
 * @param onValueChange Callback with sanitized digit-only string, max [digitCount] chars.
 * @param digitCount   Number of OTP digits (default 6).
 */
@Composable
fun ChurrosOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    digitCount: Int = 6,
) {
    val sanitized = value.filter { it.isDigit() }.take(digitCount)
    val contentDesc = stringResource(Res.string.otp_field_content_desc)

    BasicTextField(
        value = sanitized,
        onValueChange = { raw ->
            onValueChange(raw.filter { it.isDigit() }.take(digitCount))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier.semantics { contentDescription = contentDesc },
        decorationBox = {
            OtpBoxRow(value = sanitized, digitCount = digitCount)
        },
    )
}

@Composable
private fun OtpBoxRow(value: String, digitCount: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(digitCount) { index ->
            val char = value.getOrNull(index)
            val isCurrent = value.length == index
            OtpDigitBox(char = char, isCurrent = isCurrent)
        }
    }
}

@Composable
private fun OtpDigitBox(char: Char?, isCurrent: Boolean) {
    val borderColor = when {
        isCurrent -> MaterialTheme.colorScheme.primary
        char != null -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.churrosColors.divider
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(OtpDigitSize)
            .clip(ChurrosShapes.card)
            .border(
                width = OtpBorderWidth,
                color = borderColor,
                shape = ChurrosShapes.card,
            ),
    ) {
        if (char != null) {
            Text(
                text = char.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@ChurrosPreview
@Composable
fun OtpFieldPreviewContent() {
    var otp by remember { mutableStateOf("123") }
    Box(modifier = Modifier.padding(ChurrosSpacing.M)) {
        ChurrosOtpField(
            value = otp,
            onValueChange = { otp = it },
        )
    }
}
