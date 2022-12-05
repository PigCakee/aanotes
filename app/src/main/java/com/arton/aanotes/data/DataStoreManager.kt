package com.arton.aanotes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.CURRENT_NOTE_ID_KEY
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.ONBOARDING_KEY
import com.arton.aanotes.data.DataStoreManager.PreferencesKeys.SHARING_ENABLED_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val ONBOARDING_KEY = booleanPreferencesKey("userToken")
        val CURRENT_NOTE_ID_KEY = longPreferencesKey("currentNoteId")
        val SHARING_ENABLED_KEY = booleanPreferencesKey("sharingEnabledKey")
    }

    val isOnBoardingPassed: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING_KEY] ?: false
    }

    val isSharingEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SHARING_ENABLED_KEY] ?: false
    }

    val currentNoteId: Flow<Long?> = context.dataStore.data.map { preferences: Preferences ->
        preferences[CURRENT_NOTE_ID_KEY]
    }

    suspend fun setCurrentNoteId(id: Long) = context.dataStore.edit { preferences ->
        preferences[CURRENT_NOTE_ID_KEY] = id
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
