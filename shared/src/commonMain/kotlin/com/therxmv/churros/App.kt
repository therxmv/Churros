package com.therxmv.churros

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.therxmv.churros.core.design.ChurrosTheme
import com.therxmv.churros.feature.auth.domain.model.AuthState
import com.therxmv.churros.feature.auth.domain.usecase.ObserveAuthStateUseCase
import org.koin.compose.koinInject

@Composable
fun App() {
    val observeAuthState: ObserveAuthStateUseCase = koinInject()
    val authState by observeAuthState().collectAsStateWithLifecycle(initialValue = AuthState.Loading)

    ChurrosTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (authState) {
                // Session is being restored from local storage — show nothing (splash covers it).
                AuthState.Loading -> Unit

                // Authenticated: navigate to the Home graph.
                // TODO(Phase 3 — Navigation): replace with HomeGraph NavEntry.
                is AuthState.Authenticated -> HomeGraphPlaceholder()

                // Not authenticated: navigate to the Auth graph.
                // TODO(Phase 3 — Navigation): replace with AuthGraph NavEntry.
                AuthState.Unauthenticated -> AuthGraphPlaceholder()
            }
        }
    }
}

/** Placeholder shown until the Home navigation graph is implemented in Phase 3. */
@Composable
private fun HomeGraphPlaceholder() {
    // TODO(Phase 3 — Navigation): implement Home navigation graph.
}

/** Placeholder shown until the Auth navigation graph is implemented in Phase 3. */
@Composable
private fun AuthGraphPlaceholder() {
    // TODO(Phase 3 — Navigation): implement Auth navigation graph.
}
