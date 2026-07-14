package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.ic_app_logo
import churros.shared.generated.resources.ic_app_logo_24
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.design.ChurrosSpacing
import org.jetbrains.compose.resources.painterResource

private val LogoIconSizeDefault = 40.dp

@Composable
fun ChurrosLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = LogoIconSizeDefault,
    showText: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ChurrosSpacing.S),
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(Res.drawable.ic_app_logo_24),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
        )
        if (showText) {
            Text(
                text = "Churros",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun LogoPreviewContent() {
    ChurrosLogo(modifier = Modifier.padding(ChurrosSpacing.M))
}
