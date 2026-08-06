package com.therxmv.churros.feature.onboarding.presentation

/**
 * One-time side-effects emitted by [OnboardingViewModel].
 *
 * Each effect is consumed exactly once by the screen composable and triggers
 * a navigation action via the back-stack callbacks supplied by the nav graph.
 */
sealed interface OnboardingUiEffect {

    /** Navigate to the Sign In screen, clearing the onboarding back-stack. */
    data object NavigateToSignIn : OnboardingUiEffect

    /** Navigate to the Sign Up / Create Account screen, clearing the onboarding back-stack. */
    data object NavigateToSignUp : OnboardingUiEffect
}
