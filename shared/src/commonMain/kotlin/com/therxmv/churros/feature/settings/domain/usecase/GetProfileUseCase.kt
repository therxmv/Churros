package com.therxmv.churros.feature.settings.domain.usecase

import com.therxmv.churros.feature.settings.domain.model.UserProfile
import com.therxmv.churros.feature.settings.domain.repository.UserRepository

/**
 * Fetches the current authenticated user's full profile from Supabase.
 *
 * One-shot fetch — combines `public.profiles`, `public.household_members`, and the live
 * auth session. No local cache or Realtime subscription.
 */
class GetProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<UserProfile> = repository.getProfile()
}
