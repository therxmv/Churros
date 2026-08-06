package com.therxmv.churros.feature.onboarding.presentation

/**
 * Immutable UI state for the onboarding screen.
 *
 * Pager position is managed by Compose [androidx.compose.foundation.pager.PagerState] directly
 * in the screen; no page index is tracked here. This state class exists to satisfy the standard
 * Churros MVI contract and can be extended if loading/error states are needed.
 */
data class OnboardingUiState(
    val isMarkingAsSeen: Boolean = false,
)
