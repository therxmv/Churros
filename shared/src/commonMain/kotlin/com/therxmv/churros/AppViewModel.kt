package com.therxmv.churros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.churros.core.navigation.FullscreenRoute
import com.therxmv.churros.core.navigation.ScaffoldRoute
import com.therxmv.churros.feature.auth.domain.model.AuthState
import com.therxmv.churros.feature.auth.domain.usecase.ObserveAuthStateUseCase
import com.therxmv.churros.feature.onboarding.domain.usecase.IsOnboardingSeenUseCase
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * App-level ViewModel responsible for determining the initial navigation destination.
 *
 * Decision tree (runs once on startup):
 * 1. Onboarding not seen → [Onboarding1Route] (DataStore flag = false)
 * 2. Onboarding seen + session active → [HomeRoute]
 * 3. Onboarding seen + no session → [SignInRoute]
 *
 * [startDestination] emits `null` while the decision is in progress; the UI shows a splash /
 * loading indicator until a non-null value is received.
 */
class AppViewModel(
    private val observeAuthState: ObserveAuthStateUseCase,
    private val isOnboardingSeen: IsOnboardingSeenUseCase,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<NavKey?>(null)
    val startDestination: StateFlow<NavKey?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val onboardingSeen = isOnboardingSeen()
            if (!onboardingSeen) {
                _startDestination.value = FullscreenRoute.Onboarding1Route
                return@launch
            }

            // Wait for Supabase auth to finish restoring the session from storage.
            val authState = observeAuthState()
                .first { it !is AuthState.Loading }

            _startDestination.value = when (authState) {
                is AuthState.Authenticated -> ScaffoldRoute.HomeRoute
                is AuthState.Unauthenticated -> FullscreenRoute.SignInRoute
                // Loading is filtered out above; satisfy the exhaustive when.
                AuthState.Loading -> FullscreenRoute.SignInRoute
            }
        }
    }
}
