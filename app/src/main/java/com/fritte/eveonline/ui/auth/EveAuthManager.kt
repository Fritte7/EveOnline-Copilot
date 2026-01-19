package com.fritte.eveonline.ui.auth

import com.fritte.eveonline.data.network.api.EVESsoAPI
import com.fritte.eveonline.data.network.TokenStore
import com.fritte.eveonline.data.model.eve.EveAuthConfig
import com.fritte.eveonline.data.model.eve.buildAuthorizeUri
import com.fritte.eveonline.utils.Pkce
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class EveAuthManager(
    private val cfg: EveAuthConfig,
    private val ssoApi: EVESsoAPI,
    private val tokenStore: TokenStore,
) {
    private val mutex = Mutex()

    // temporary values for one login attempt
    @Volatile private var pendingVerifier: String? = null
    @Volatile private var pendingState: String? = null

    fun startLogin(): Pair<android.net.Uri, Unit> {
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

        tokenStore.saveTokens(tokens.access_token, tokens.refresh_token)

        // Clear pending values (one-time use)
        pendingState = null
        pendingVerifier = null

        return true
    }
}
