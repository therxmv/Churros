package com.therxmv.churros.feature.family.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseholdDao {

    /**
     * Emits the household row for [id] whenever it changes in Room.
     * Emits `null` if the row has not yet been cached.
     */
    @Query("SELECT * FROM households WHERE id = :id")
    fun observeHousehold(id: String): Flow<HouseholdEntity?>

    /** Inserts or replaces the household row. */
    @Upsert
    suspend fun upsert(household: HouseholdEntity)

    /** Removes the household row (e.g. when a Realtime DELETE event arrives). */
    @Query("DELETE FROM households WHERE id = :id")
    suspend fun deleteById(id: String)
}
