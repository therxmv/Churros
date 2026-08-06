package com.therxmv.churros.feature.onboarding.presentation

/**
 * User-initiated actions on the onboarding screen.
 *
 * All three events end the onboarding flow and mark it as seen via
 * [com.therxmv.churros.feature.onboarding.domain.usecase.MarkOnboardingSeenUseCase].
 */
sealed interface OnboardingUiEvent {

    /** User tapped "Skip" on slide 1 or 2 — navigate to Sign In. */
    data object SkipClicked : OnboardingUiEvent

    /** User tapped "Sign In" on slide 3 — navigate to Sign In. */
    data object NavigateToSignInClicked : OnboardingUiEvent

    /** User tapped "Create Account" on slide 3 — navigate to Sign Up. */
    data object NavigateToSignUpClicked : OnboardingUiEvent
}
