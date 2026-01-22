package com.fritte.eveonline.data.model.auth

import android.net.Uri
import com.fritte.eveonline.data.network.api.EVESsoAPI
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Base64

class EveAuthManager(
    private val cfg: EveAuthConfig,
    private val ssoApi: EVESsoAPI,
    private val tokenStore: DataStoreTokenRepository,
    private val moshi: Moshi,
) {
    private val mutex = Mutex()

    // temporary values for one login attempt
    @Volatile private var pendingVerifier: String? = null
    @Volatile private var pendingState: String? = null

    fun startLogin(): Pair<Uri, Unit> {
        val pkce = Pkce.generate()
        pendingVerifier = pkce.verifier
        pendingState = pkce.state
        val uri = buildAuthorizeUri(cfg, pkce)
        return uri to Unit
    }

    suspend fun handleAuthCode(code: String, returnedState: String): Boolean = mutex.withLock {
        val expectedState = pendingState
        val verifier = pendingVerifier

        if (expectedState.isNullOrBlank() || verifier.isNullOrBlank()) return false
        if (returnedState != expectedState) return false

        val tokens = ssoApi.requestToken(
            code = code,
            clientId = cfg.clientId,
            codeVerifier = verifier,
            redirectUri = cfg.redirectUri,
        )

        val claims = parseJwtClaims(tokens.access_token, moshi)
        val characterId = characterIdFromSub(claims.sub)
        val characterName = claims.name ?: "Unknown"

        tokenStore.saveTokens(tokens.access_token, tokens.refresh_token)
        tokenStore.saveSession(characterId, characterName)

        // Clear pending values (one-time use)
        pendingState = null
        pendingVerifier = null

        return true
    }

    private fun parseJwtClaims(token: String, moshi: Moshi): EveJwtClaims {
        val parts = token.split(".")
        require(parts.size >= 2) { "Invalid JWT" }

        val payloadJson = String(
            Base64.getUrlDecoder().decode(parts[1]),
            Charsets.UTF_8
        )

        return moshi.adapter(EveJwtClaims::class.java)
            .fromJson(payloadJson)
            ?: error("Failed to parse JWT claims")
    }

    private fun characterIdFromSub(sub: String): Long {
        // sub = "CHARACTER:EVE:123456789"
        return sub.substringAfterLast(":").toLong()
    }

}