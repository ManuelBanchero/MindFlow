package com.example.mindflow.data.local.preferences.impl

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.mindflow.data.local.preferences.SessionPreferencesDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SESSION_PREFERENCES_NAME = "session_preferences"
private val Context.sessionPreferencesDataStore by preferencesDataStore(name = SESSION_PREFERENCES_NAME)

class DataStoreSessionPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : SessionPreferencesDataSource {

    private companion object {
        val SESSION_ACTIVE_KEY = booleanPreferencesKey("session_active")
    }

    override suspend fun isSessionActive(): Boolean {
        return context.sessionPreferencesDataStore.data
            .map { preferences -> preferences[SESSION_ACTIVE_KEY] ?: false }
            .first()
    }

    override suspend fun setSessionActive(isActive: Boolean) {
        context.sessionPreferencesDataStore.edit { preferences ->
            preferences[SESSION_ACTIVE_KEY] = isActive
        }
    }
}
