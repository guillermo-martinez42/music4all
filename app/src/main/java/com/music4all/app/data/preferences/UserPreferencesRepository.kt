package com.music4all.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.music4all.app.domain.MusicService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Repository wrapping Jetpack DataStore for user preference storage.
 */
class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val PREFERRED_SERVICE_KEY = stringPreferencesKey("preferred_service")
        private val ONBOARDING_COMPLETE_KEY = booleanPreferencesKey("onboarding_complete")
    }

    /**
     * Emits the user's currently preferred music service.
     * Defaults to SPOTIFY if no preference is saved.
     */
    val preferredServiceFlow: Flow<MusicService> = context.dataStore.data.map { prefs ->
        val serviceName = prefs[PREFERRED_SERVICE_KEY] ?: MusicService.SPOTIFY.name
        try {
            MusicService.valueOf(serviceName)
        } catch (_: IllegalArgumentException) {
            MusicService.SPOTIFY
        }
    }

    /**
     * Whether the user has completed the onboarding (selected a service at least once).
     */
    val isOnboardingCompleteFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETE_KEY] ?: false
    }

    /**
     * Saves the user's preferred music service and marks onboarding as complete.
     */
    suspend fun setPreferredService(service: MusicService) {
        context.dataStore.edit { prefs ->
            prefs[PREFERRED_SERVICE_KEY] = service.name
            prefs[ONBOARDING_COMPLETE_KEY] = true
        }
    }

    /**
     * One-shot read of the preferred service (for use in non-Flow contexts).
     */
    suspend fun getPreferredService(): MusicService {
        return preferredServiceFlow.first()
    }
}
