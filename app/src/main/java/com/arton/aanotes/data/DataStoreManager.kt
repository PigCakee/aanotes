package com.arton.aanotes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.AUTH_COOLDOWN_KEY
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.CURRENT_NOTE_ID_KEY
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.SCREEN_CAPTURE_ENABLED_KEY
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.SHARING_ENABLED_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val CURRENT_NOTE_ID_KEY = longPreferencesKey("currentNoteId")
        val SHARING_ENABLED_KEY = booleanPreferencesKey("sharingEnabledKey")
        val SCREEN_CAPTURE_ENABLED_KEY = booleanPreferencesKey("captureEnabledKey")
        val AUTH_COOLDOWN_KEY = intPreferencesKey("cooldownKey")
    }

    val isSharingEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHARING_ENABLED_KEY] ?: false
    }

    val isScreenCaptureEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SCREEN_CAPTURE_ENABLED_KEY] ?: false
    }

    val authCooldownSeconds: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[AUTH_COOLDOWN_KEY] ?: 5
    }

    val currentNoteId: Flow<Long?> = context.dataStore.data.map { preferences: Preferences ->
        preferences[CURRENT_NOTE_ID_KEY]
    }

    suspend fun setCurrentNoteId(id: Long) = context.dataStore.edit { preferences ->
        preferences[CURRENT_NOTE_ID_KEY] = id
    }

    suspend fun setSharingEnabled(enabled: Boolean) = context.dataStore.edit { preferences ->
        preferences[SHARING_ENABLED_KEY] = enabled
    }

    suspend fun setScreenCaptureEnabled(enabled: Boolean) = context.dataStore.edit { preferences ->
        preferences[SCREEN_CAPTURE_ENABLED_KEY] = enabled
    }

    suspend fun setAuthCooldownSeconds(seconds: Int) = context.dataStore.edit { preferences ->
        preferences[AUTH_COOLDOWN_KEY] = seconds
    }

    suspend fun clearAll() = context.dataStore.edit { preferences ->
        preferences[AUTH_COOLDOWN_KEY] = 5
        preferences[SCREEN_CAPTURE_ENABLED_KEY] = false
        preferences[SHARING_ENABLED_KEY] = false
        preferences[CURRENT_NOTE_ID_KEY] = 0
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
