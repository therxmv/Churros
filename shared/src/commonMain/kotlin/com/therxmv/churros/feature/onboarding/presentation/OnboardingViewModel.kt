package com.therxmv.churros.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.churros.feature.onboarding.domain.usecase.MarkOnboardingSeenUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the onboarding carousel screen.
 *
 * Responsibilities:
 * - Persists the first-launch flag via [MarkOnboardingSeenUseCase] before navigating away.
 * - Translates [OnboardingUiEvent]s into [OnboardingUiEffect]s so the screen can trigger
 *   navigation without holding a reference to the back-stack.
 *
 * Pager page state lives in Compose ([androidx.compose.foundation.pager.PagerState]); this
 * ViewModel does not track the current slide index.
 */
class OnboardingViewModel(
    private val markOnboardingSeen: MarkOnboardingSeenUseCase,
) : ViewModel() {

    private val _effects = Channel<OnboardingUiEffect>(Channel.BUFFERED)
    val effects: Flow<OnboardingUiEffect> = _effects.receiveAsFlow()

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.SkipClicked,
            OnboardingUiEvent.NavigateToSignInClicked -> finishOnboarding(navigateToSignUp = false)
            OnboardingUiEvent.NavigateToSignUpClicked -> finishOnboarding(navigateToSignUp = true)
        }
    }

    private fun finishOnboarding(navigateToSignUp: Boolean) {
        viewModelScope.launch {
            markOnboardingSeen()
            val effect = if (navigateToSignUp) {
                OnboardingUiEffect.NavigateToSignUp
            } else {
                OnboardingUiEffect.NavigateToSignIn
            }
            _effects.send(effect)
        }
    }
}
