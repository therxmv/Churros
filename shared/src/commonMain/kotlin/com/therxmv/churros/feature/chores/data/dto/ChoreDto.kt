package com.therxmv.churros.feature.chores.data.dto

import com.therxmv.churros.feature.chores.data.local.ChoreEntity
import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import com.therxmv.churros.feature.chores.domain.model.toChorePriority
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Supabase Postgrest / Realtime wire DTO for the `public.chores` table.
 *
 * All timestamp fields arrive as ISO-8601 strings from PostgREST. Snake-case
 * column names are mapped to camelCase Kotlin properties via [SerialName].
 */
@Serializable
data class ChoreDto(
    val id: String,
    @SerialName("household_id") val householdId: String,
    @SerialName("created_by") val createdBy: String? = null,
    @SerialName("assignee_id") val assigneeId: String? = null,
    val title: String,
    val category: String? = null,
    /** One of "low", "medium", "high" — matches the `chore_priority` Postgres enum. */
    val priority: String,
    @SerialName("reward_points") val rewardPoints: Int,
    @SerialName("due_at") val dueAt: String? = null,
    @SerialName("repeat_schedule") val repeatSchedule: String? = null,
    /** Null means the chore is still open. */
    @SerialName("completed_at") val completedAt: String? = null,
    @SerialName("created_at") val createdAt: String,
)

// ---------------------------------------------------------------------------
// Mapping helpers
// ---------------------------------------------------------------------------

internal fun ChoreDto.toDomain(): Chore = Chore(
    id = id,
    householdId = householdId,
    createdBy = createdBy,
    assigneeId = assigneeId,
    title = title,
    category = category,
    priority = priority.toChorePriority(),
    rewardPoints = rewardPoints,
    dueAt = dueAt?.let { Instant.parse(it) },
    repeatSchedule = repeatSchedule,
    completedAt = completedAt?.let { Instant.parse(it) },
    createdAt = Instant.parse(createdAt),
)

internal fun ChoreDto.toEntity(): ChoreEntity = ChoreEntity(
    id = id,
    householdId = householdId,
    createdBy = createdBy,
    assigneeId = assigneeId,
    title = title,
    category = category,
    priority = priority,
    rewardPoints = rewardPoints,
    dueAt = dueAt?.let { Instant.parse(it).toEpochMilliseconds() },
    repeatSchedule = repeatSchedule,
    completedAt = completedAt?.let { Instant.parse(it).toEpochMilliseconds() },
    createdAt = Instant.parse(createdAt).toEpochMilliseconds(),
)

