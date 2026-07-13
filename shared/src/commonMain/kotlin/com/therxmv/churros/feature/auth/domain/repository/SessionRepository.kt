package com.therxmv.churros.feature.auth.domain.repository

interface SessionRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun saveSession()
    suspend fun clearSession()
}
