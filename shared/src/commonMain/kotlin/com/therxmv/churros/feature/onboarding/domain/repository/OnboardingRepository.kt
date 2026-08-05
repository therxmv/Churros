package com.therxmv.churros.feature.onboarding.domain.repository

/**
 * Manages the persistent first-launch onboarding flag.
 *
 * The flag gates whether the app shows the Onboarding slides on startup.
 * Once the user completes or skips onboarding, [markOnboardingSeen] is called and the slides
 * are never shown again.
 */
interface OnboardingRepository {

    /**
     * Returns `true` if the user has already seen the onboarding slides at least once.
     */
    suspend fun isOnboardingSeen(): Boolean

    /**
     * Persists the onboarding-seen flag so [isOnboardingSeen] returns `true` on future launches.
     */
    suspend fun markOnboardingSeen()
}
