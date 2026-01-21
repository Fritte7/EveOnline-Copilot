package com.fritte.eveonline.data.repo

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "eve_session")

class DataStoreTokenRepositoryImpl(
    private val context: Context
) : DataStoreTokenRepository {

    private val KEY_ACCESS = stringPreferencesKey("access_token")
    private val KEY_REFRESH = stringPreferencesKey("refresh_token")
    private val KEY_CHARACTER_ID = longPreferencesKey("character_id")
    private val KEY_CHARACTER_NAME = stringPreferencesKey("character_name")

    override val hasSessionFlow: Flow<Boolean> =
        context.dataStore.data.map { prefs -> !prefs[KEY_REFRESH].isNullOrBlank() }

    override val characterIdFlow: Flow<Long?> =
        context.dataStore.data.map { prefs -> prefs[KEY_CHARACTER_ID] }

    override val characterNameFlow: Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[KEY_CHARACTER_NAME] }

    override suspend fun getAccessToken(): String? =
        context.dataStore.data.map { it[KEY_ACCESS] }.firstOrNull()

    override suspend fun getRefreshToken(): String? =
        context.dataStore.data.map { it[KEY_REFRESH] }.firstOrNull()

    override suspend fun getCharacterId(): Long? =
        context.dataStore.data.map { it[KEY_CHARACTER_ID] }.firstOrNull()

    override suspend fun getCharacterName(): String? =
        context.dataStore.data.map { it[KEY_CHARACTER_NAME] }.firstOrNull()

    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS] = accessToken
            if (refreshToken != null) prefs[KEY_REFRESH] = refreshToken
        }
    }

    override suspend fun saveSession(characterID: Long, characterName: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CHARACTER_ID] = characterID
            prefs[KEY_CHARACTER_NAME] = characterName
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
