package com.therxmv.churros.feature.notifications.data.dto

import com.therxmv.churros.feature.notifications.domain.model.Notification
import com.therxmv.churros.feature.notifications.domain.model.toNotificationType
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Supabase PostgREST / Realtime wire DTO for a row in `public.notifications`.
 *
 * [payload] is a JSONB column. Its keys vary by [type] (e.g. "chore_title",
 * "requester_name") and are flattened to [Map<String, String>] in [toDomain].
 *
 * All timestamp fields arrive as ISO-8601 strings from PostgREST.
 */
@Serializable
data class NotificationDto(
    val id: String,
    @SerialName("recipient_id") val recipientId: String,
    @SerialName("household_id") val householdId: String,
    /** String value matching the `notification_type` Postgres enum. */
    val type: String,
    /** Context-specific JSONB; keys vary by [type]. Defaults to empty object. */
    val payload: JsonObject = JsonObject(emptyMap()),
    @SerialName("is_read") val isRead: Boolean,
    @SerialName("created_at") val createdAt: String,
)

// ---------------------------------------------------------------------------
// Mapping helpers
// ---------------------------------------------------------------------------

internal fun NotificationDto.toDomain(): Notification = Notification(
    id = id,
    type = type.toNotificationType(),
    payload = payload.entries.associate { (key, element) ->
        key to runCatching { element.jsonPrimitive.content }.getOrDefault("")
    },
    isRead = isRead,
    createdAt = Instant.parse(createdAt),
)
