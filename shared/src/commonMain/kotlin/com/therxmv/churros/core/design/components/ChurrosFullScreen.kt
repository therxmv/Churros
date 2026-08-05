package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Base container for all [com.therxmv.churros.core.navigation.FullscreenRoute] destinations.
 *
 * Renders a [Scaffold] without any navigation chrome — no bottom nav bar, no top app bar.
 * Used for the Onboarding flow and the Auth flow (Sign In, Sign Up, Verify Email, Set New
 * Password).
 *
 * Feature screens fill the content slot and receive the scaffold's inner padding.
 *
 * @param modifier Applied to the underlying [Scaffold].
 * @param content  The screen content that fills the full scaffold body.
 */
@Composable
fun ChurrosFullScreen(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        content = content,
    )
}
