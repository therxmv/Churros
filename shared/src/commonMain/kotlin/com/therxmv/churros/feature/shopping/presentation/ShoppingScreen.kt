package com.therxmv.churros.feature.shopping.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import churros.shared.generated.resources.Res
import churros.shared.generated.resources.nav_shopping
import churros.shared.generated.resources.placeholder_coming_soon
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShoppingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${stringResource(Res.string.nav_shopping)} — ${stringResource(Res.string.placeholder_coming_soon)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
