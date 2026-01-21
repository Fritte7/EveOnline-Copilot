package com.fritte.eveonline.data.network

import com.fritte.eveonline.data.network.api.EVESsoAPI
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlinx.coroutines.runBlocking

class EveTokenAuthenticator(
    private val tokenStore: DataStoreTokenRepository,
    private val ssoApi: EVESsoAPI,
    private val clientId: String,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refresh = runBlocking { tokenStore.getRefreshToken() } ?: return null

        val newTokens = runBlocking {
            ssoApi.refreshToken(refreshToken = refresh, clientId = clientId)
        }

        runBlocking {
            tokenStore.saveTokens(
                accessToken = newTokens.access_token,
                refreshToken = newTokens.refresh_token
            )
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokens.access_token}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}
