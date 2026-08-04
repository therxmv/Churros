package com.therxmv.churros.feature.family.domain.repository

import com.therxmv.churros.feature.family.domain.model.Household
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface FamilyRepository {

    /**
     * Returns a cold [Flow] that emits the authenticated user's household.
     * Room is the source of truth; Realtime events from Supabase keep it in sync.
     *
     * Emits `null` if the household row has not yet been cached.
     * The initial emission comes from Room; a full Supabase fetch runs concurrently
     * to populate Room on first collection.
     *
     * The flow's coroutine scope manages the Realtime channel lifecycle.
     */
    fun observeHousehold(): Flow<Household?>

    /**
     * Returns a cold [Flow] that emits all members in the authenticated user's household
     * as [UserProfile] instances.
     * Room is the source of truth; Realtime events from Supabase keep it in sync.
     *
     * Each [UserProfile] includes denormalised profile data (display name, avatar) from the
     * local Room cache. Email, push token, and notification preferences are not cached and
     * will be null / defaults unless fetched via
     * [com.therxmv.churros.feature.settings.domain.usecase.GetProfileUseCase].
     *
     * The flow's coroutine scope manages the Realtime channel lifecycle.
     */
    fun observeMembers(): Flow<List<UserProfile>>

    /**
     * Updates the household's mutable fields ([name], [address]).
     * Only household parents are permitted by RLS.
     * Returns the updated [Household] on success.
     */
    suspend fun updateHousehold(name: String, address: String?): Result<Household>

    /**
     * Removes a member from the household.
     * Only household parents are permitted by RLS.
     * A parent cannot remove themselves — that requires household deletion or transfer.
     */
    suspend fun removeMember(userId: String): Result<Unit>

    /**
     * Uploads [imageBytes] to the `family-photos` Supabase Storage bucket and stores
     * the resulting URL in `households.photo_url`.
     *
     * The file is written to path `<household_id>/photo.jpg` (upsert — any previous
     * cover photo for this household is overwritten).
     *
     * Returns the updated [Household] (with the new [Household.photoUrl]) on success.
     */
    suspend fun uploadHouseholdPhoto(imageBytes: ByteArray): Result<Household>

    // NOTE: UpdateMemberPermissions is not yet applicable.
    // The `household_members` schema does not currently contain per-member permission
    // columns (require_chore_approval, can_self_assign, can_claim_rewards).
    // This will be added once the schema is extended. Tracked in issue #58.
}
