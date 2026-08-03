package com.therxmv.churros.feature.auth.data.repository

import com.therxmv.churros.feature.auth.domain.model.AuthError
import com.therxmv.churros.feature.auth.domain.model.AuthState
import com.therxmv.churros.feature.auth.domain.model.AuthUser
import com.therxmv.churros.feature.auth.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

class SupabaseAuthRepository(
    private val supabaseClient: SupabaseClient,
) : AuthRepository {

    override fun observeAuthState(): Flow<AuthState> =
        supabaseClient.auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val user = status.session.user
                    if (user != null) {
                        AuthState.Authenticated(user.toDomain())
                    } else {
                        AuthState.Unauthenticated
                    }
                }

                is SessionStatus.NotAuthenticated -> AuthState.Unauthenticated

                // Session is being loaded from on-device storage on startup.
                SessionStatus.Initializing -> AuthState.Loading

                // Token refresh failed (e.g. network unavailable or token revoked).
                // Treat as unauthenticated so the app navigates to the Auth graph and
                // the user can re-authenticate.
                is SessionStatus.RefreshFailure -> AuthState.Unauthenticated
            }
        }

    override suspend fun getCurrentUser(): AuthUser? =
        supabaseClient.auth.currentUserOrNull()?.toDomain()

    override suspend fun signInWithEmail(
        email: String,
        password: String,
    ): Result<AuthUser> = runCatching {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        supabaseClient.auth.currentUserOrNull()?.toDomain()
            // TODO(Phase 3 — Localization): localize error message
            ?: throw AuthError.Unknown(message = "User not found after sign-in")
    }.mapAuthError()

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
    ): Result<AuthUser> = runCatching {
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
        // After sign-up with email confirmation enabled the session may not yet be active,
        // but the user object is still accessible if the provider returned one.
        supabaseClient.auth.currentUserOrNull()?.toDomain()
            // TODO(Phase 3 — Localization): localize error message
            ?: throw AuthError.Unknown(message = "User not found after sign-up")
    }.mapAuthError()

    override suspend fun signInWithGoogle(idToken: String): Result<AuthUser> =
        runCatching {
            supabaseClient.auth.signInWith(IDToken) {
                provider = Google
                this.idToken = idToken
            }
            supabaseClient.auth.currentUserOrNull()?.toDomain()
                // TODO(Phase 3 — Localization): localize error message
                ?: throw AuthError.Unknown(message = "User not found after Google sign-in")
        }.mapAuthError()

    override suspend fun signInWithApple(idToken: String): Result<AuthUser> {
        // TODO: Implement Apple SSO in the iOS stabilisation phase.
        //  Requires Apple OAuth certificates configured in the Supabase dashboard —
        //  see supabase/config.toml for context.
        // TODO(Phase 3 — Localization): localize error message
        return Result.failure(
            AuthError.Unknown(message = "Apple Sign-In is not yet implemented"),
        )
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        supabaseClient.auth.signOut()
    }.mapAuthError()

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun UserInfo.toDomain(): AuthUser = AuthUser(
        id = id,
        email = email,
        displayName = userMetadata?.get("full_name")?.jsonPrimitive?.contentOrNull
            ?: userMetadata?.get("name")?.jsonPrimitive?.contentOrNull
            ?: email?.substringBefore("@"),
        avatarUrl = userMetadata?.get("avatar_url")?.jsonPrimitive?.contentOrNull
            ?: userMetadata?.get("picture")?.jsonPrimitive?.contentOrNull,
    )

    /**
     * Maps any Supabase / network exception to a typed [AuthError] so callers
     * receive a domain-layer error rather than a raw SDK exception.
     *
     * Supabase HTTP status codes used here:
     *  - 400 → invalid credentials (wrong email / password)
     *  - 422 → validation error (email already in use, weak password, …)
     */
    private fun Throwable.toAuthError(): AuthError = when {
        this is AuthError -> this
        this is HttpRequestException -> AuthError.NetworkError
        this is RestException && statusCode == 400 -> AuthError.InvalidCredentials
        this is RestException && statusCode == 422 -> AuthError.EmailAlreadyInUse
        else -> AuthError.Unknown(message = message, cause = this)
    }

    private fun <T> Result<T>.mapAuthError(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { Result.failure(it.toAuthError()) },
    )
}
