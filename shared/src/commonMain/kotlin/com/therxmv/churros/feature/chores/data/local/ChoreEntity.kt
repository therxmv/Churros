package com.therxmv.churros.feature.chores.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import kotlinx.datetime.Instant

/**
 * Room entity for the local chores cache.
 *
 * Timestamps are stored as epoch-millisecond [Long] values for efficient querying.
 * The `priority` field is stored as a string matching the Postgres enum values
 * ("low" / "medium" / "high") for a lossless round-trip.
 */
@Entity(tableName = "chores")
data class ChoreEntity(
    @PrimaryKey val id: String,
    val householdId: String,
    val createdBy: String?,
    val assigneeId: String?,
    val title: String,
    val category: String?,
    /** One of "low", "medium", "high". */
    val priority: String,
    val rewardPoints: Int,
    /** Epoch millis; null means no due date. */
    val dueAt: Long?,
    val repeatSchedule: String?,
    /** Epoch millis; null means the chore is still open. */
    val completedAt: Long?,
    /** Epoch millis. */
    val createdAt: Long,
)

// ---------------------------------------------------------------------------
// Mapping helper
// ---------------------------------------------------------------------------

internal fun ChoreEntity.toDomain(): Chore = Chore(
    id = id,
    householdId = householdId,
    createdBy = createdBy,
    assigneeId = assigneeId,
    title = title,
    category = category,
    priority = when (priority.lowercase()) {
        "low" -> ChorePriority.LOW
        "high" -> ChorePriority.HIGH
        else -> ChorePriority.MEDIUM
    },
    rewardPoints = rewardPoints,
    dueAt = dueAt?.let { Instant.fromEpochMilliseconds(it) },
    repeatSchedule = repeatSchedule,
    completedAt = completedAt?.let { Instant.fromEpochMilliseconds(it) },
    createdAt = Instant.fromEpochMilliseconds(createdAt),
)
