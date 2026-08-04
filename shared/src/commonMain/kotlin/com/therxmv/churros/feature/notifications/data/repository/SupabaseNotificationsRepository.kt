package com.therxmv.churros.feature.notifications.data.repository

import co.touchlab.kermit.Logger
import com.therxmv.churros.feature.notifications.data.dto.NotificationDto
import com.therxmv.churros.feature.notifications.data.dto.toDomain
import com.therxmv.churros.feature.notifications.domain.model.Notification
import com.therxmv.churros.feature.notifications.domain.model.NotificationError
import com.therxmv.churros.feature.notifications.domain.model.NotificationFeed
import com.therxmv.churros.feature.notifications.domain.repository.NotificationsRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

class SupabaseNotificationsRepository(
    private val supabaseClient: SupabaseClient,
) : NotificationsRepository {

    private val logger = Logger.withTag("SupabaseNotificationsRepository")

    /**
     * Lenient Json decoder for Realtime event records.
     * [Json.ignoreUnknownKeys] keeps the client forward-compatible when Supabase
     * adds columns without a corresponding client release.
     */
    private val realtimeJson = Json { ignoreUnknownKeys = true }

    // ---------------------------------------------------------------------------
    // getNotificationFeed
    // ---------------------------------------------------------------------------

    override fun getNotificationFeed(): Flow<NotificationFeed> = channelFlow {
        val userId = requireUserId()

        // 1. Fetch the initial notification list. Failures are logged and swallowed
        //    so that a transient network error does not break the Realtime subscription.
        val initial = runCatching { fetchNotifications(userId) }
            .onFailure { logger.e("Initial notifications fetch failed: $it") }
            .getOrDefault(emptyList())

        // Mutable snapshot maintained in memory. All Realtime mutations are applied
        // to this list before re-grouping and re-emitting to the collector.
        val items = initial.toMutableList()
        send(groupIntoFeed(items))

        // 2. Open a dedicated Realtime channel for this screen.
        //    The channel name uses `notifications_feed:` (vs `notifications:` in the
        //    Home feature) to avoid conflicts when both screens are on the back-stack.
        val realtimeChannel = supabaseClient.channel("notifications_feed:$userId")
        val changeFlow = realtimeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "notifications"
            filter("recipient_id", FilterOperator.EQ, userId)
        }

        // 3. Subscribe and apply incoming Realtime events in a background coroutine.
        launch {
            runCatching { realtimeChannel.subscribe() }
                .onFailure { logger.e("Notifications Realtime subscribe failed: $it") }

            runCatching {
                changeFlow.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            // A new notification arrived: prepend it to the in-memory list
                            // so it appears at the top of the Recent bucket.
                            val dto = runCatching {
                                realtimeJson.decodeFromJsonElement<NotificationDto>(action.record)
                            }.getOrNull() ?: return@collect

                            items.add(0, dto.toDomain())
                            send(groupIntoFeed(items))
                        }

                        is PostgresAction.Update -> {
                            // An existing notification was updated (e.g. is_read flipped).
                            // Find the item and replace it in-place to reflect the new state.
                            val dto = runCatching {
                                realtimeJson.decodeFromJsonElement<NotificationDto>(action.record)
                            }.getOrNull() ?: return@collect

                            val updated = dto.toDomain()
                            val index = items.indexOfFirst { it.id == updated.id }
                            if (index >= 0) {
                                items[index] = updated
                                send(groupIntoFeed(items))
                            }
                        }

                        // DELETE is handled by the TTL pg_cron job, not by clients.
                        else -> Unit
                    }
                }
            }.onFailure { logger.e("Notifications Realtime stream error: $it") }
        }

        // 4. Suspend until the collector cancels.
        try {
            kotlinx.coroutines.awaitCancellation()
        } finally {
            // 5. Clean up the channel when the flow scope is cancelled.
            withContext(NonCancellable) {
                runCatching { supabaseClient.realtime.removeChannel(realtimeChannel) }
                    .onFailure { logger.e("Failed to remove notifications_feed Realtime channel: $it") }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // markAsRead
    // ---------------------------------------------------------------------------

    override suspend fun markAsRead(notificationId: String): Result<Unit> = runCatching {
        requireUserId() // verify session is still valid before writing
        supabaseClient.postgrest.from("notifications")
            .update(MarkReadDto(isRead = true)) {
                filter { eq("id", notificationId) }
            }
        Unit
    }.mapNotificationError()

    // ---------------------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------------------

    private fun requireUserId(): String =
        supabaseClient.auth.currentUserOrNull()?.id ?: throw NotificationError.Unauthorized

    /**
     * Fetches all notifications for [userId] from Supabase, ordered newest-first.
     * Limited to 100 rows to cap the initial payload; the Realtime subscription handles
     * items arriving after the initial fetch.
     */
    private suspend fun fetchNotifications(userId: String): List<Notification> =
        supabaseClient.postgrest.from("notifications")
            .select {
                filter { eq("recipient_id", userId) }
                order("created_at", Order.DESCENDING)
                limit(100)
            }
            .decodeList<NotificationDto>()
            .map { it.toDomain() }

    /**
     * Partitions [items] into [NotificationFeed.recent] (created within the last 24 h)
     * and [NotificationFeed.earlier]. Both buckets preserve the original list order
     * (newest-first from the Supabase query).
     */
    @OptIn(ExperimentalTime::class)
    private fun groupIntoFeed(items: List<Notification>): NotificationFeed {
        val cutoff = Clock.System.now().minus(24.hours)
        val (recent, earlier) = items.partition { it.createdAt > cutoff }
        return NotificationFeed(recent = recent, earlier = earlier)
    }

    private fun Throwable.toNotificationError(): NotificationError = when {
        this is NotificationError -> this
        this is HttpRequestException -> NotificationError.NetworkError
        this is RestException && statusCode == 401 -> NotificationError.Unauthorized
        this is RestException && statusCode == 403 -> NotificationError.Unauthorized
        else -> NotificationError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapNotificationError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toNotificationError()) },
    )

    // ---------------------------------------------------------------------------
    // Internal write DTOs
    // ---------------------------------------------------------------------------

    /** Partial update DTO — only `is_read` is sent to Supabase. */
    @Serializable
    private data class MarkReadDto(
        @SerialName("is_read") val isRead: Boolean,
    )
}
