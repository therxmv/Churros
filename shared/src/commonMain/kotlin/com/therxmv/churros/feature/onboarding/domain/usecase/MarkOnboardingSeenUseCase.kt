package com.therxmv.churros.feature.onboarding.domain.usecase

import com.therxmv.churros.feature.onboarding.domain.repository.OnboardingRepository

/**
 * Marks the onboarding flow as seen so it is never shown again on subsequent launches.
 *
 * Called by the Onboarding presentation layer when the user completes the last slide or taps
 * "Skip". After this call, [IsOnboardingSeenUseCase] will return `true`.
 */
class MarkOnboardingSeenUseCase(private val repository: OnboardingRepository) {

    suspend operator fun invoke() = repository.markOnboardingSeen()
}
