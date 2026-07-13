package com.therxmv.churros.feature.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.therxmv.churros.feature.auth.domain.repository.SessionRepository
import kotlinx.coroutines.flow.first

class SessionRepositoryImpl(private val dataStore: DataStore<Preferences>) : SessionRepository {

    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")

    override suspend fun isLoggedIn(): Boolean =
        dataStore.data.first()[isLoggedInKey] ?: false

    override suspend fun saveSession() {
        dataStore.edit { it[isLoggedInKey] = true }
    }

    override suspend fun clearSession() {
        dataStore.edit { it.remove(isLoggedInKey) }
    }
}
