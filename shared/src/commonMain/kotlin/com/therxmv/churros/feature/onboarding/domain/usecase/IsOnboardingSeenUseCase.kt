package com.therxmv.churros.feature.onboarding.domain.usecase

import com.therxmv.churros.feature.onboarding.domain.repository.OnboardingRepository

/**
 * Checks whether the user has previously completed the onboarding flow.
 *
 * Used at app startup by [com.therxmv.churros.AppViewModel] to decide the initial destination:
 * - `false` → start at [com.therxmv.churros.core.navigation.Onboarding1Route]
 * - `true`  → check auth state to determine Home or Sign In
 */
class IsOnboardingSeenUseCase(private val repository: OnboardingRepository) {

    suspend operator fun invoke(): Boolean = repository.isOnboardingSeen()
}
