package com.takumi.kasirkain.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "token_prefs",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "token_prefs"))
    }
)

class TokenManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    suspend fun getToken(): String? {
        return context.tokenDataStore.data
            .map { preferences -> preferences[ACCESS_TOKEN_KEY] }
            .firstOrNull()
    }

    suspend fun saveToken(accessToken: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun clearToken() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }
}