package com.therxmv.churros.core.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.therxmv.churros.core.design.ChurrosPreview
import com.therxmv.churros.core.design.ChurrosPreviewWrapper
import com.therxmv.churros.core.navigation.ScaffoldRoute

/**
 * Base container for all [ScaffoldRoute] destinations.
 *
 * Renders a [Scaffold] with the Churros bottom navigation bar ([ChurrosBottomNavBar]).
 * Feature screens fill the content slot and receive the scaffold's inner padding.
 *
 * Tab navigation is handled here: tapping a bottom nav tab either pushes a new destination
 * onto the back stack or pops back to an existing one (handled via [onTabSelected]).
 *
 * @param currentRoute   The currently visible scaffold destination (used to highlight the active
 *   tab).
 * @param onTabSelected  Called with the tab's root route when the user taps a bottom nav item.
 * @param modifier       Applied to the underlying [Scaffold].
 * @param content        The screen content that fills the scaffold body.
 */
@Composable
fun ChurrosScaffoldScreen(
    currentRoute: ScaffoldRoute,
    onTabSelected: (ScaffoldRoute) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            ChurrosBottomNavBar(
                currentRoute = currentRoute,
                onTabSelected = onTabSelected,
            )
        },
        content = content,
    )
}

@PreviewWrapper(ChurrosPreviewWrapper::class)
@ChurrosPreview
@Composable
fun ScaffoldScreenPreviewContent() {
    ChurrosScaffoldScreen(currentRoute = ScaffoldRoute.HomeRoute, onTabSelected = {}) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Screen content", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
