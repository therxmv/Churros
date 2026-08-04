package com.therxmv.churros.feature.family.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyMemberDao {

    /**
     * Emits all members for [householdId] whenever the cache changes.
     * Results are ordered by [FamilyMemberEntity.joinedAt] ascending (oldest member first).
     */
    @Query("SELECT * FROM family_members WHERE householdId = :householdId ORDER BY joinedAt ASC")
    fun observeMembers(householdId: String): Flow<List<FamilyMemberEntity>>

    /** Inserts or replaces a batch of members (used for the initial Supabase sync). */
    @Upsert
    suspend fun upsertAll(members: List<FamilyMemberEntity>)

    /** Inserts or replaces a single member (used for Realtime Insert / Update events). */
    @Upsert
    suspend fun upsert(member: FamilyMemberEntity)

    /** Removes a member by their user UUID (used for Realtime Delete events and [removeMember]). */
    @Query("DELETE FROM family_members WHERE userId = :userId")
    suspend fun deleteById(userId: String)

    /**
     * Removes all cached members for a household.
     * Useful when the user leaves a household or the cache needs to be invalidated.
     */
    @Query("DELETE FROM family_members WHERE householdId = :householdId")
    suspend fun deleteAllByHousehold(householdId: String)
}
