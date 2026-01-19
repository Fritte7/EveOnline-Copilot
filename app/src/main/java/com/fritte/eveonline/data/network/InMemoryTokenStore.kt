package com.fritte.eveonline.data.network

interface TokenStore {
    suspend fun hasRefreshToken(): Boolean
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String?)

    suspend fun saveSession(characterID: Long, characterName: String)
}

class InMemoryTokenStore : TokenStore {
    private var access: String? = null
    private var refresh: String? = null
    private var characterId: Long? = null
    private var characterName: String? = null


    override suspend fun getAccessToken() = access
    override suspend fun getRefreshToken() = refresh

    override suspend fun hasRefreshToken(): Boolean = !refresh.isNullOrBlank()

    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        access = accessToken
        if (refreshToken != null) refresh = refreshToken
    }

    override suspend fun saveSession(characterID: Long, characterName: String) {
        this.characterId = characterID
        this.characterName = characterName
    }
}
