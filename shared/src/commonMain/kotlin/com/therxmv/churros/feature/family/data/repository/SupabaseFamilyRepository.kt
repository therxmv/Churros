package com.therxmv.churros.feature.family.data.repository

import co.touchlab.kermit.Logger
import com.therxmv.churros.feature.family.data.dto.FamilyMemberDto
import com.therxmv.churros.feature.family.data.dto.HouseholdDto
import com.therxmv.churros.feature.family.data.dto.toDomain
import com.therxmv.churros.feature.family.data.dto.toEntity
import com.therxmv.churros.feature.family.data.local.FamilyMemberDao
import com.therxmv.churros.feature.family.data.local.HouseholdDao
import com.therxmv.churros.feature.family.data.local.toDomain
import com.therxmv.churros.feature.family.domain.model.FamilyError
import com.therxmv.churros.feature.family.domain.model.Household
import com.therxmv.churros.feature.family.domain.repository.FamilyRepository
import com.therxmv.churros.feature.settings.domain.model.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.storage
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

class SupabaseFamilyRepository(
    private val supabaseClient: SupabaseClient,
    private val householdDao: HouseholdDao,
    private val familyMemberDao: FamilyMemberDao,
) : FamilyRepository {

    private val logger = Logger.withTag("SupabaseFamilyRepository")

    /**
     * Lenient Json instance for decoding Realtime event records.
     * `ignoreUnknownKeys` ensures forward-compatibility when Supabase adds columns.
     */
    private val realtimeJson = Json { ignoreUnknownKeys = true }

    // Cache the household_id after the first lookup so we avoid an extra network call
    // on every operation. Protected by a Mutex to prevent duplicate fetches when
    // multiple coroutines call getHouseholdId() concurrently.
    private val householdIdMutex = Mutex()
    private var cachedHouseholdId: String? = null

    // ---------------------------------------------------------------------------
    // observeHousehold
    // ---------------------------------------------------------------------------

    override fun observeHousehold(): Flow<Household?> = channelFlow {
        val householdId = getHouseholdId()

        // 1. Prime the Room cache with a fresh Supabase fetch (best-effort).
        runCatching { initialSyncHousehold(householdId) }
            .onFailure { logger.e("Household initial sync failed: $it") }

        // 2. Register Realtime listener BEFORE subscribing so no events are missed.
        val realtimeChannel = supabaseClient.channel("households:$householdId")
        val changeFlow = realtimeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "households"
            filter("id", FilterOperator.EQ, householdId)
        }

        // 3. Subscribe and forward Realtime events into Room in a background coroutine.
        launch {
            runCatching { realtimeChannel.subscribe() }
                .onFailure { logger.e("Household Realtime subscribe failed: $it") }

            runCatching {
                changeFlow.collect { action ->
                    handleHouseholdRealtimeAction(action, householdId)
                }
            }.onFailure { logger.e("Household Realtime stream error: $it") }
        }

        try {
            // 4. Room is the single source of truth — emit its live value to the caller.
            householdDao.observeHousehold(householdId)
                .map { it?.toDomain() }
                .collect { send(it) }
        } finally {
            // 5. Clean up the channel when the collector cancels.
            withContext(NonCancellable) {
                runCatching { supabaseClient.realtime.removeChannel(realtimeChannel) }
                    .onFailure { logger.e("Failed to unsubscribe household Realtime channel: $it") }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // observeMembers
    // ---------------------------------------------------------------------------

    override fun observeMembers(): Flow<List<UserProfile>> = channelFlow {
        val householdId = getHouseholdId()

        // 1. Prime the Room cache.
        runCatching { initialSyncMembers(householdId) }
            .onFailure { logger.e("Members initial sync failed: $it") }

        // 2. Register Realtime listener.
        val realtimeChannel = supabaseClient.channel("household_members:$householdId")
        val changeFlow = realtimeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "household_members"
            filter("household_id", FilterOperator.EQ, householdId)
        }

        // 3. Subscribe and forward Realtime events into Room.
        launch {
            runCatching { realtimeChannel.subscribe() }
                .onFailure { logger.e("Members Realtime subscribe failed: $it") }

            runCatching {
                changeFlow.collect { action ->
                    handleMemberRealtimeAction(action)
                }
            }.onFailure { logger.e("Members Realtime stream error: $it") }
        }

        try {
            // 4. Room is the single source of truth.
            familyMemberDao.observeMembers(householdId)
                .map { entities -> entities.map { it.toDomain() } }
                .collect { send(it) }
        } finally {
            withContext(NonCancellable) {
                runCatching { supabaseClient.realtime.removeChannel(realtimeChannel) }
                    .onFailure { logger.e("Failed to unsubscribe members Realtime channel: $it") }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // Mutations
    // ---------------------------------------------------------------------------

    override suspend fun updateHousehold(name: String, address: String?): Result<Household> =
        runCatching {
            val householdId = getHouseholdId()
            val dto = supabaseClient.postgrest.from("households")
                .update(UpdateHouseholdDto(name = name, address = address)) {
                    filter { eq("id", householdId) }
                    select()
                }
                .decodeSingle<HouseholdDto>()
            householdDao.upsert(dto.toEntity())
            dto.toDomain()
        }.mapFamilyError()

    override suspend fun removeMember(userId: String): Result<Unit> = runCatching {
        val householdId = getHouseholdId()
        supabaseClient.postgrest.from("household_members")
            .delete {
                filter {
                    eq("user_id", userId)
                    eq("household_id", householdId)
                }
            }
        familyMemberDao.deleteById(userId)
    }.mapFamilyError()

    override suspend fun uploadHouseholdPhoto(imageBytes: ByteArray): Result<Household> =
        runCatching {
            val householdId = getHouseholdId()
            val path = "$householdId/photo.jpg"

            // Upload to the private `family-photos` bucket; upsert overwrites any
            // previous cover photo for this household.
            supabaseClient.storage.from("family-photos").upload(path, imageBytes) {
                upsert = true
            }

            // Retrieve the stable storage URL and persist it in the `households` row.
            val photoUrl = supabaseClient.storage.from("family-photos").publicUrl(path)
            val dto = supabaseClient.postgrest.from("households")
                .update(UpdatePhotoDto(photoUrl = photoUrl)) {
                    filter { eq("id", householdId) }
                    select()
                }
                .decodeSingle<HouseholdDto>()
            householdDao.upsert(dto.toEntity())
            dto.toDomain()
        }.mapFamilyError()

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
                ?: throw FamilyError.HouseholdNotFound

            val member = supabaseClient.postgrest.from("household_members")
                .select {
                    filter { eq("user_id", userId) }
                    limit(1)
                }
                .decodeSingleOrNull<HouseholdMemberLookupDto>()
                ?: throw FamilyError.HouseholdNotFound

            member.householdId.also { cachedHouseholdId = it }
        }
    }

    /** Fetches the household from Supabase and upserts it into Room. */
    private suspend fun initialSyncHousehold(householdId: String) {
        val dto = supabaseClient.postgrest.from("households")
            .select { filter { eq("id", householdId) } }
            .decodeSingleOrNull<HouseholdDto>() ?: return
        householdDao.upsert(dto.toEntity())
    }

    /**
     * Fetches all members for [householdId] from Supabase (with embedded profiles join)
     * and upserts them into Room.
     */
    private suspend fun initialSyncMembers(householdId: String) {
        val remote = supabaseClient.postgrest.from("household_members")
            .select(Columns.raw("*,profiles(display_name,avatar_url)")) {
                filter { eq("household_id", householdId) }
            }
            .decodeList<FamilyMemberDto>()
        familyMemberDao.upsertAll(remote.map { it.toEntity() })
    }

    /**
     * Processes a Realtime postgres-change event for the `households` table and
     * reflects it in Room.
     */
    private suspend fun handleHouseholdRealtimeAction(
        action: PostgresAction,
        householdId: String,
    ) {
        runCatching {
            when (action) {
                is PostgresAction.Insert -> {
                    val dto = realtimeJson.decodeFromJsonElement<HouseholdDto>(action.record)
                    householdDao.upsert(dto.toEntity())
                }
                is PostgresAction.Update -> {
                    val dto = realtimeJson.decodeFromJsonElement<HouseholdDto>(action.record)
                    householdDao.upsert(dto.toEntity())
                }
                is PostgresAction.Delete -> {
                    // Household was deleted — clear the cache.
                    householdDao.deleteById(householdId)
                    cachedHouseholdId = null
                }
                else -> Unit
            }
        }.onFailure { logger.e("Failed to handle household Realtime action $action: $it") }
    }

    /**
     * Processes a Realtime postgres-change event for the `household_members` table and
     * reflects it in Room.
     *
     * For Insert / Update events, the raw Realtime record does NOT include the embedded
     * profiles join. We therefore re-fetch the full member row (with profile) from
     * Supabase before upserting into Room.
     *
     * For Delete events, Postgres sends only the primary key columns in the old record
     * (REPLICA IDENTITY DEFAULT), so only `user_id` is guaranteed to be present.
     */
    private suspend fun handleMemberRealtimeAction(action: PostgresAction) {
        runCatching {
            when (action) {
                is PostgresAction.Insert, is PostgresAction.Update -> {
                    val record = if (action is PostgresAction.Insert) {
                        action.record
                    } else {
                        (action as PostgresAction.Update).record
                    }
                    val minimal = realtimeJson.decodeFromJsonElement<MemberRecord>(record)
                    // Re-fetch with profiles join to include display_name and avatar_url.
                    val dto = supabaseClient.postgrest.from("household_members")
                        .select(Columns.raw("*,profiles(display_name,avatar_url)")) {
                            filter { eq("user_id", minimal.userId) }
                            limit(1)
                        }
                        .decodeSingleOrNull<FamilyMemberDto>() ?: return
                    familyMemberDao.upsert(dto.toEntity())
                }
                is PostgresAction.Delete -> {
                    val record = realtimeJson.decodeFromJsonElement<MemberRecord>(action.oldRecord)
                    familyMemberDao.deleteById(record.userId)
                }
                else -> { /* no-op for Select and any future action types */ }
            }
        }.onFailure { logger.e("Failed to handle member Realtime action $action: $it") }
    }

    private fun Throwable.toFamilyError(): FamilyError = when {
        this is FamilyError -> this
        this is HttpRequestException -> FamilyError.NetworkError
        this is RestException && statusCode == 401 -> FamilyError.Unauthorized
        this is RestException && statusCode == 403 -> FamilyError.Unauthorized
        this is RestException && statusCode == 404 -> FamilyError.MemberNotFound
        else -> FamilyError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapFamilyError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toFamilyError()) },
    )

    // ---------------------------------------------------------------------------
    // Internal DTOs (not exposed outside this file)
    // ---------------------------------------------------------------------------

    /** Minimal DTO used to resolve the current user's household_id. */
    @Serializable
    private data class HouseholdMemberLookupDto(
        @SerialName("household_id") val householdId: String,
    )

    /** Minimal DTO used to extract the user_id from a Realtime household_members event. */
    @Serializable
    private data class MemberRecord(
        @SerialName("user_id") val userId: String,
    )

    @Serializable
    private data class UpdateHouseholdDto(
        val name: String,
        val address: String?,
    )

    @Serializable
    private data class UpdatePhotoDto(
        @SerialName("photo_url") val photoUrl: String,
    )
}
