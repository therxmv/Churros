package com.therxmv.churros.feature.chores.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoreDao {

    /**
     * Emits the full chore list for [householdId] whenever the cache changes.
     * Results are ordered by due date ascending (nulls last).
     */
    @Query(
        """
        SELECT * FROM chores
        WHERE householdId = :householdId
        ORDER BY
            CASE WHEN dueAt IS NULL THEN 1 ELSE 0 END,
            dueAt ASC
        """,
    )
    fun observeChores(householdId: String): Flow<List<ChoreEntity>>

    /** Inserts or replaces a batch of chores (used for the initial Supabase sync). */
    @Upsert
    suspend fun upsertAll(chores: List<ChoreEntity>)

    /** Inserts or replaces a single chore (used for Realtime Insert / Update events). */
    @Upsert
    suspend fun upsert(chore: ChoreEntity)

    /** Removes a chore by its primary key (used for Realtime Delete events). */
    @Query("DELETE FROM chores WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Removes all cached chores for a household.
     * Useful when the user switches households or the cache needs to be invalidated.
     */
    @Query("DELETE FROM chores WHERE householdId = :householdId")
    suspend fun deleteAllByHousehold(householdId: String)
}
