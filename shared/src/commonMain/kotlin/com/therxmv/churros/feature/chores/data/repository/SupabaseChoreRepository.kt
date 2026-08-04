package com.therxmv.churros.feature.chores.data.repository

import co.touchlab.kermit.Logger
import com.therxmv.churros.feature.chores.data.dto.ChoreDto
import com.therxmv.churros.feature.chores.data.dto.HouseholdMemberDto
import com.therxmv.churros.feature.chores.data.dto.toDto
import com.therxmv.churros.feature.chores.data.dto.toDomain
import com.therxmv.churros.feature.chores.data.dto.toEntity
import com.therxmv.churros.feature.chores.data.local.ChoreDao
import com.therxmv.churros.feature.chores.data.local.toDomain
import com.therxmv.churros.feature.chores.domain.model.Chore
import com.therxmv.churros.feature.chores.domain.model.ChoreError
import com.therxmv.churros.feature.chores.domain.model.ChorePriority
import com.therxmv.churros.feature.chores.domain.repository.ChoreRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class SupabaseChoreRepository(
    private val supabaseClient: SupabaseClient,
    private val choreDao: ChoreDao,
) : ChoreRepository {

    private val logger = Logger.withTag("SupabaseChoreRepository")

    /**
     * Lenient Json instance for decoding Realtime event records.
     * `ignoreUnknownKeys` ensures forward-compatibility when Supabase adds columns.
     */
    private val realtimeJson = Json { ignoreUnknownKeys = true }

    // Cache the household_id after the first lookup so we avoid an extra network
    // call on every operation. Protected by a Mutex to prevent duplicate fetches
    // when multiple coroutines call getHouseholdId() concurrently.
    private val householdIdMutex = Mutex()
    private var cachedHouseholdId: String? = null

    // ---------------------------------------------------------------------------
    // observeChores
    // ---------------------------------------------------------------------------

    override fun observeChores(): Flow<List<Chore>> = channelFlow {
        val householdId = getHouseholdId()

        // 1. Prime the Room cache with a full Supabase fetch (best-effort).
        //    If this fails, Room may already have stale data from a previous session.
        runCatching { initialSync(householdId) }
            .onFailure { logger.e("Initial sync failed: $it") }

        // 2. Register Realtime listener BEFORE subscribing so no events are missed.
        //    `supabaseClient.channel()` is an extension on SupabaseClient that routes
        //    to the Realtime plugin.
        val realtimeChannel = supabaseClient.channel("chores:$householdId")
        val changeFlow = realtimeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "chores"
            filter("household_id", FilterOperator.EQ, householdId)
        }

        // 3. Subscribe and forward Realtime events into Room in a background coroutine.
        launch {
            runCatching { realtimeChannel.subscribe() }
                .onFailure { logger.e("Realtime subscribe failed: $it") }

            runCatching {
                changeFlow.collect { action ->
                    handleRealtimeAction(action)
                }
            }.onFailure { logger.e("Realtime stream error: $it") }
        }

        try {
            // 4. Room is the single source of truth — emit its live list to the caller.
            choreDao.observeChores(householdId)
                .map { entities -> entities.map { it.toDomain() } }
                .collect { send(it) }
        } finally {
            // 5. Clean up the channel when the collector cancels.
            withContext(NonCancellable) {
                runCatching { supabaseClient.realtime.removeChannel(realtimeChannel) }
                    .onFailure { logger.e("Failed to unsubscribe Realtime channel: $it") }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // CRUD
    // ---------------------------------------------------------------------------

    override suspend fun createChore(
        title: String,
        category: String?,
        assigneeId: String?,
        dueAt: Instant?,
        repeatSchedule: String?,
        priority: ChorePriority,
        rewardPoints: Int,
    ): Result<Chore> = runCatching {
        val householdId = getHouseholdId()
        val dto = supabaseClient.postgrest.from("chores")
            .insert(
                CreateChoreDto(
                    householdId = householdId,
                    title = title,
                    category = category,
                    assigneeId = assigneeId,
                    dueAt = dueAt?.toString(),
                    repeatSchedule = repeatSchedule,
                    priority = priority.toDto(),
                    rewardPoints = rewardPoints,
                ),
            ) { select() }
            .decodeSingle<ChoreDto>()
        choreDao.upsert(dto.toEntity())
        dto.toDomain()
    }.mapChoreError()

    override suspend fun updateChore(
        id: String,
        title: String,
        category: String?,
        assigneeId: String?,
        dueAt: Instant?,
        repeatSchedule: String?,
        priority: ChorePriority,
        rewardPoints: Int,
    ): Result<Chore> = runCatching {
        val dto = supabaseClient.postgrest.from("chores")
            .update(
                UpdateChoreDto(
                    title = title,
                    category = category,
                    assigneeId = assigneeId,
                    dueAt = dueAt?.toString(),
                    repeatSchedule = repeatSchedule,
                    priority = priority.toDto(),
                    rewardPoints = rewardPoints,
                ),
            ) {
                filter { eq("id", id) }
                select()
            }
            .decodeSingle<ChoreDto>()
        choreDao.upsert(dto.toEntity())
        dto.toDomain()
    }.mapChoreError()

    @OptIn(ExperimentalTime::class)
    override suspend fun completeChore(id: String): Result<Chore> = runCatching {
        val now = Clock.System.now().toString()
        val dto = supabaseClient.postgrest.from("chores")
            .update(CompleteChoreDto(completedAt = now)) {
                filter { eq("id", id) }
                select()
            }
            .decodeSingle<ChoreDto>()
        choreDao.upsert(dto.toEntity())
        dto.toDomain()
    }.mapChoreError()

    override suspend fun deleteChore(id: String): Result<Unit> = runCatching {
        supabaseClient.postgrest.from("chores")
            .delete { filter { eq("id", id) } }
        choreDao.deleteById(id)
    }.mapChoreError()

    // ---------------------------------------------------------------------------
    // Private helpers
    // ---------------------------------------------------------------------------

    /**
     * Fetches and caches the authenticated user's household_id from
     * `public.household_members`. Thread-safe via [Mutex].
     */
    private suspend fun getHouseholdId(): String = householdIdMutex.withLock {
        cachedHouseholdId ?: run {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw ChoreError.Unauthorized

            val member = supabaseClient.postgrest.from("household_members")
                .select {
                    filter { eq("user_id", userId) }
                    limit(1)
                }
                .decodeSingleOrNull<HouseholdMemberDto>()
                ?: throw ChoreError.HouseholdNotFound

            member.householdId.also { cachedHouseholdId = it }
        }
    }

    /**
     * Fetches all chores for [householdId] from Supabase and upserts them into Room.
     */
    private suspend fun initialSync(householdId: String) {
        val remote = supabaseClient.postgrest.from("chores")
            .select { filter { eq("household_id", householdId) } }
            .decodeList<ChoreDto>()
        choreDao.upsertAll(remote.map { it.toEntity() })
    }

    /**
     * Processes a single Realtime postgres-change event and reflects it in Room.
     *
     * `record` / `oldRecord` are raw [kotlinx.serialization.json.JsonObject] values.
     * For DELETE events, Postgres sends only the primary key in the old record when the
     * table uses the default REPLICA IDENTITY (not FULL), so only `id` is guaranteed.
     */
    private suspend fun handleRealtimeAction(action: PostgresAction) {
        runCatching {
            when (action) {
                is PostgresAction.Insert -> {
                    val dto = realtimeJson.decodeFromJsonElement<ChoreDto>(action.record)
                    choreDao.upsert(dto.toEntity())
                }
                is PostgresAction.Update -> {
                    val dto = realtimeJson.decodeFromJsonElement<ChoreDto>(action.record)
                    choreDao.upsert(dto.toEntity())
                }
                is PostgresAction.Delete -> {
                    val record = realtimeJson.decodeFromJsonElement<ChoreDeleteRecord>(action.oldRecord)
                    choreDao.deleteById(record.id)
                }
                else -> Unit
            }
        }.onFailure { logger.e("Failed to handle Realtime action $action: $it") }
    }

    private fun Throwable.toChoreError(): ChoreError = when {
        this is ChoreError -> this
        this is HttpRequestException -> ChoreError.NetworkError
        this is RestException && statusCode == 401 -> ChoreError.Unauthorized
        this is RestException && statusCode == 403 -> ChoreError.Unauthorized
        this is RestException && statusCode == 404 -> ChoreError.NotFound
        else -> ChoreError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapChoreError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toChoreError()) },
    )

    // ---------------------------------------------------------------------------
    // Internal write DTOs (not exposed outside this file)
    // ---------------------------------------------------------------------------

    @Serializable
    private data class CreateChoreDto(
        @SerialName("household_id") val householdId: String,
        val title: String,
        val category: String?,
        @SerialName("assignee_id") val assigneeId: String?,
        @SerialName("due_at") val dueAt: String?,
        @SerialName("repeat_schedule") val repeatSchedule: String?,
        val priority: String,
        @SerialName("reward_points") val rewardPoints: Int,
    )

    @Serializable
    private data class UpdateChoreDto(
        val title: String,
        val category: String?,
        @SerialName("assignee_id") val assigneeId: String?,
        @SerialName("due_at") val dueAt: String?,
        @SerialName("repeat_schedule") val repeatSchedule: String?,
        val priority: String,
        @SerialName("reward_points") val rewardPoints: Int,
    )

    @Serializable
    private data class CompleteChoreDto(
        @SerialName("completed_at") val completedAt: String,
    )

    /** Minimal record for extracting the primary key from a Realtime DELETE event. */
    @Serializable
    private data class ChoreDeleteRecord(val id: String)
}
