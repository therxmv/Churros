package com.therxmv.churros.feature.settings.domain.usecase

import com.therxmv.churros.feature.settings.domain.model.NotificationPreferences
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import com.therxmv.churros.feature.settings.domain.repository.UserRepository

/**
 * Persists the user's per-type notification toggles to the `notification_prefs` JSONB
 * column in `public.profiles` (added by migration `10_notification_prefs.sql`).
 *
 * Returns the updated [UserProfile] (including the new [UserProfile.notificationPreferences])
 * on success.
 */
class UpdateNotificationPreferencesUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(preferences: NotificationPreferences): Result<UserProfile> =
        repository.updateNotificationPreferences(preferences = preferences)
}
