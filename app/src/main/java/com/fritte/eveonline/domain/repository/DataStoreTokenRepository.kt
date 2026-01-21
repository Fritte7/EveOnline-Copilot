package com.fritte.eveonline.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreTokenRepository {
    val hasSessionFlow: Flow<Boolean>
    val characterIdFlow: Flow<Long?>
    val characterNameFlow: Flow<String?>

    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getCharacterId(): Long?
    suspend fun getCharacterName(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String?)
    suspend fun saveSession(characterID: Long, characterName: String)
}