package com.therxmv.churros.feature.family.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.churros.feature.family.domain.model.Household
import kotlinx.datetime.Instant

/**
 * Room entity for the local household cache.
 *
 * The `createdAt` timestamp is stored as epoch milliseconds for efficient Room queries.
 */
@Entity(tableName = "households")
data class HouseholdEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String?,
    val photoUrl: String?,
    /** Epoch millis. */
    val createdAt: Long,
)

// ---------------------------------------------------------------------------
// Mapping helper
// ---------------------------------------------------------------------------

internal fun HouseholdEntity.toDomain(): Household = Household(
    id = id,
    name = name,
    address = address,
    photoUrl = photoUrl,
    createdAt = Instant.fromEpochMilliseconds(createdAt),
)
