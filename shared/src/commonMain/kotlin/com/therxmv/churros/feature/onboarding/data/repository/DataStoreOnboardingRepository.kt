package com.therxmv.churros.feature.onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.therxmv.churros.feature.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.first

/**
 * DataStore-backed implementation of [OnboardingRepository].
 *
 * Uses the shared [DataStore] instance (provided by the platform DI module) to persist the
 * onboarding-seen flag across app launches and process restarts.
 */
class DataStoreOnboardingRepository(
    private val dataStore: DataStore<Preferences>,
) : OnboardingRepository {

    override suspend fun isOnboardingSeen(): Boolean =
        dataStore.data.first()[ONBOARDING_SEEN_KEY] ?: false

    override suspend fun markOnboardingSeen() {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_SEEN_KEY] = true
        }
    }

    companion object {
        private val ONBOARDING_SEEN_KEY = booleanPreferencesKey("onboarding_seen")
    }
}
