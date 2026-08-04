package com.therxmv.churros.feature.home.data.repository

import co.touchlab.kermit.Logger
import com.therxmv.churros.feature.chores.data.dto.ChoreDto
import com.therxmv.churros.feature.chores.data.dto.toDomain
import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.home.data.dto.NotificationDto
import com.therxmv.churros.feature.home.data.dto.toDomain
import com.therxmv.churros.feature.home.domain.model.ActivityItem
import com.therxmv.churros.feature.home.domain.model.ChoreProgress
import com.therxmv.churros.feature.home.domain.model.DashboardData
import com.therxmv.churros.feature.home.domain.model.HomeError
import com.therxmv.churros.feature.home.domain.repository.HomeRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SupabaseHomeRepository(
    private val supabaseClient: SupabaseClient,
) : HomeRepository {

    private val logger = Logger.withTag("SupabaseHomeRepository")

    /**
     * Lenient Json instance for decoding Realtime event records.
     * `ignoreUnknownKeys` ensures forward-compatibility when Supabase adds columns.
     */
    private val realtimeJson = Json { ignoreUnknownKeys = true }

    // Cache the household_id after the first lookup to avoid repeated network calls.
    // Protected by a Mutex to prevent duplicate fetches when multiple coroutines call
    // getHouseholdId() concurrently.
    private val householdIdMutex = Mutex()
    private var cachedHouseholdId: String? = null

    // ---------------------------------------------------------------------------
    // getDashboardData
    // ---------------------------------------------------------------------------

    @OptIn(ExperimentalTime::class)
    override suspend fun getDashboardData(): Result<DashboardData> = runCatching {
        val userId = requireUserId()
        val householdId = getHouseholdId(userId)

        // Compute today's UTC boundary for the server-side due_at filter.
        val (todayStart, tomorrowStart) = todayUtcBounds()

        // Fetch household name and current user display name concurrently.
        val householdName = fetchHouseholdName(householdId)
        val currentUserName = fetchUserDisplayName(userId)

        // Fetch all chores due today for this household.
        val todayChores = fetchTodayChores(
            householdId = householdId,
            todayStart = todayStart,
            tomorrowStart = tomorrowStart,
        )

        // Derive progress counts client-side.
        val myChores = todayChores.filter { it.assigneeId == userId }
        val personalProgress = ChoreProgress(
            completed = myChores.count { it.completedAt != null },
            total = myChores.size,
        )
        val familyProgress = ChoreProgress(
            completed = todayChores.count { it.completedAt != null },
            total = todayChores.size,
        )

        // Upcoming = open chores sorted by due time (nulls last).
        val upcomingChores = todayChores
            .filter { it.completedAt == null }
            .sortedWith(compareBy(nullsLast()) { it.dueAt })

        DashboardData(
            familyName = householdName,
            currentUserName = currentUserName,
            personalProgress = personalProgress,
            familyProgress = familyProgress,
            upcomingChores = upcomingChores,
        )
    }.mapHomeError()

    // ---------------------------------------------------------------------------
    // observeActivityFeed
    // ---------------------------------------------------------------------------

    override fun observeActivityFeed(): Flow<List<ActivityItem>> = channelFlow {
        val userId = requireUserId()

        // 1. Load initial notification list from Supabase.
        val initial = runCatching { fetchNotifications(userId) }
            .onFailure { logger.e("Initial notifications fetch failed: $it") }
            .getOrDefault(emptyList())

        // Mutable snapshot kept in memory; prepended as new Realtime items arrive.
        val items = initial.toMutableList()
        send(items.toList())

        // 2. Register Realtime listener BEFORE subscribing so no events are missed.
        //    Channel name follows the `notifications:<user_id>` convention from the schema.
        val realtimeChannel = supabaseClient.channel("notifications:$userId")
        val changeFlow = realtimeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "notifications"
            filter("recipient_id", io.github.jan.supabase.postgrest.query.filter.FilterOperator.EQ, userId)
        }

        // 3. Subscribe and prepend new items as they arrive.
        launch {
            runCatching { realtimeChannel.subscribe() }
                .onFailure { logger.e("Notifications Realtime subscribe failed: $it") }

            runCatching {
                changeFlow.collect { action ->
                    when (action) {
                        is PostgresAction.Insert -> {
                            val dto = runCatching {
                                realtimeJson.decodeFromJsonElement<NotificationDto>(action.record)
                            }.getOrNull() ?: return@collect

                            items.add(0, dto.toDomain())
                            send(items.toList())
                        }
                        // UPDATE (mark-as-read) and DELETE are not needed for the feed display.
                        else -> Unit
                    }
                }
            }.onFailure { logger.e("Notifications Realtime stream error: $it") }
        }

        // 4. Suspend until the collector cancels.
        try {
            kotlinx.coroutines.awaitCancellation()
        } finally {
            // 5. Clean up the channel when the collector cancels.
            withContext(NonCancellable) {
                runCatching { supabaseClient.realtime.removeChannel(realtimeChannel) }
                    .onFailure { logger.e("Failed to unsubscribe notifications Realtime channel: $it") }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------------------

    private fun requireUserId(): String =
        supabaseClient.auth.currentUserOrNull()?.id ?: throw HomeError.Unauthorized

    /**
     * Fetches and caches the authenticated user's household_id.
     * Thread-safe via [Mutex].
     */
    private suspend fun getHouseholdId(userId: String): String = householdIdMutex.withLock {
        cachedHouseholdId ?: run {
            val member = supabaseClient.postgrest.from("household_members")
                .select {
                    filter { eq("user_id", userId) }
                    limit(1)
                }
                .decodeSingleOrNull<HouseholdMemberLookupDto>()
                ?: throw HomeError.HouseholdNotFound

            member.householdId.also { cachedHouseholdId = it }
        }
    }

    /** Fetches the household name for [householdId]. */
    private suspend fun fetchHouseholdName(householdId: String): String {
        val dto = supabaseClient.postgrest.from("households")
            .select(Columns.list("name")) {
                filter { eq("id", householdId) }
                limit(1)
            }
            .decodeSingle<HouseholdNameDto>()
        return dto.name
    }

    /** Fetches the display name for the given profile [userId]. */
    private suspend fun fetchUserDisplayName(userId: String): String {
        val dto = supabaseClient.postgrest.from("profiles")
            .select(Columns.list("display_name")) {
                filter { eq("id", userId) }
                limit(1)
            }
            .decodeSingle<ProfileDisplayNameDto>()
        return dto.displayName
    }

    /**
     * Fetches all chores for [householdId] that are due today (between [todayStart]
     * inclusive and [tomorrowStart] exclusive, as ISO-8601 UTC strings).
     */
    private suspend fun fetchTodayChores(
        householdId: String,
        todayStart: String,
        tomorrowStart: String,
    ): List<Chore> =
        supabaseClient.postgrest.from("chores")
            .select {
                filter {
                    eq("household_id", householdId)
                    gte("due_at", todayStart)
                    lt("due_at", tomorrowStart)
                }
            }
            .decodeList<ChoreDto>()
            .map { it.toDomain() }

    /**
     * Fetches recent notifications addressed to [userId], ordered newest-first.
     * Limited to 50 items to keep the initial payload small.
     */
    private suspend fun fetchNotifications(userId: String): List<ActivityItem> =
        supabaseClient.postgrest.from("notifications")
            .select {
                filter { eq("recipient_id", userId) }
                order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                limit(50)
            }
            .decodeList<NotificationDto>()
            .map { it.toDomain() }

    /**
     * Returns the UTC timestamps (as ISO-8601 strings) that bound today in the device's
     * current system timezone: [start, end) where end is the start of tomorrow.
     */
    @OptIn(ExperimentalTime::class)
    private fun todayUtcBounds(): Pair<String, String> {
        val tz = TimeZone.currentSystemDefault()
        val today: LocalDate = Clock.System.now().toLocalDateTime(tz).date
        val start = today.atStartOfDayIn(tz).toString()
        val end = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(tz).toString()
        return start to end
    }

    private fun Throwable.toHomeError(): HomeError = when {
        this is HomeError -> this
        this is HttpRequestException -> HomeError.NetworkError
        this is RestException && statusCode == 401 -> HomeError.Unauthorized
        this is RestException && statusCode == 403 -> HomeError.Unauthorized
        this is RestException && statusCode == 404 -> HomeError.HouseholdNotFound
        else -> HomeError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapHomeError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toHomeError()) },
    )

    // ---------------------------------------------------------------------------
    // Internal read DTOs (not exposed outside this file)
    // ---------------------------------------------------------------------------

    /** Minimal DTO used to resolve the current user's household_id. */
    @Serializable
    private data class HouseholdMemberLookupDto(
        @SerialName("household_id") val householdId: String,
    )

    /** Minimal DTO to fetch only the household name. */
    @Serializable
    private data class HouseholdNameDto(val name: String)

    /** Minimal DTO to fetch only a user's display name. */
    @Serializable
    private data class ProfileDisplayNameDto(
        @SerialName("display_name") val displayName: String,
    )
}
