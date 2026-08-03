package com.therxmv.churros.feature.auth.domain.repository

import com.therxmv.churros.feature.auth.domain.model.AuthState
import com.therxmv.churros.feature.auth.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /**
     * Emits the current [AuthState] and any subsequent changes.
     * Always starts with [AuthState.Loading] while the session is restored from storage.
     */
    fun observeAuthState(): Flow<AuthState>

    /**
     * Returns the currently signed-in user, or `null` when unauthenticated.
     */
    suspend fun getCurrentUser(): AuthUser?

    /**
     * Signs in using email and password credentials.
     */
    suspend fun signInWithEmail(email: String, password: String): Result<AuthUser>

    /**
     * Creates a new account with the given email and password.
     * Email confirmation is required when enabled in the Supabase project.
     */
    suspend fun signUpWithEmail(email: String, password: String): Result<AuthUser>

    /**
     * Signs in using a Google ID token obtained from Android Credential Manager.
     * The token acquisition itself is platform-specific and handled at the presentation layer.
     */
    suspend fun signInWithGoogle(idToken: String): Result<AuthUser>

    /**
     * Signs in using an Apple ID token.
     * TODO: Implement in the iOS stabilisation phase.
     */
    suspend fun signInWithApple(idToken: String): Result<AuthUser>

    /**
     * Signs out the current user and clears the local session.
     */
    suspend fun signOut(): Result<Unit>
}
