package com.therxmv.churros.feature.chores.domain.model

import kotlinx.datetime.Instant

/**
 * Domain model representing a single chore in a household.
 *
 * All timestamps are [Instant] (UTC). The UI layer is responsible for localising
 * them to the device's timezone for display.
 *
 * @property id             Supabase UUID primary key.
 * @property householdId    UUID of the household this chore belongs to.
 * @property createdBy      Profile UUID of the user who created the chore, or null if deleted.
 * @property assigneeId     Profile UUID of the assigned member, or null if unassigned.
 * @property title          Human-readable chore name (e.g. "Vacuum living room").
 * @property category       Optional free-text category label (e.g. "cleaning", "cooking").
 * @property priority       Urgency level.
 * @property rewardPoints   Non-negative points awarded upon completion.
 * @property dueAt          Optional deadline; null means no due date.
 * @property repeatSchedule Optional RRULE string or shorthand (daily/weekly/monthly).
 * @property completedAt    Completion timestamp; null means the chore is still open.
 * @property createdAt      When the chore was created in Supabase.
 */
data class Chore(
    val id: String,
    val householdId: String,
    val createdBy: String?,
    val assigneeId: String?,
    val title: String,
    val category: String?,
    val priority: ChorePriority,
    val rewardPoints: Int,
    val dueAt: Instant?,
    val repeatSchedule: String?,
    val completedAt: Instant?,
    val createdAt: Instant,
)
