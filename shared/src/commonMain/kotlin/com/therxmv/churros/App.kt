package com.therxmv.churros

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.therxmv.churros.core.design.ChurrosTheme
import com.therxmv.churros.core.navigation.ChurrosNavGraph
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root composable — entry point for both Android and iOS.
 *
 * Applies [ChurrosTheme], resolves the start destination via [AppViewModel], and hands off
 * rendering to [ChurrosNavGraph] once the destination is known.
 *
 * While [AppViewModel] is loading the onboarding flag and waiting for the Supabase session to
 * restore, a centered [CircularProgressIndicator] is shown. This typically resolves in one frame
 * on returning users and a handful of frames on cold launches.
 */
@Composable
fun App() {
    ChurrosTheme {
        val viewModel = koinViewModel<AppViewModel>()
        val startDestination by viewModel.startDestination.collectAsState()

        val dest = startDestination
        if (dest == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            ChurrosNavGraph(startDestination = dest)
        }
    }
}
