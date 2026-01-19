package com.fritte.eveonline.data.network

import com.fritte.eveonline.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor(
    private val tokenStore: TokenStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = runCatching { kotlinx.coroutines.runBlocking { tokenStore.getAccessToken() } }.getOrNull()

        val newReq = if (!token.isNullOrBlank()) {
            req.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("User-Agent", "${BuildConfig.EVE_APP_NAME}/${BuildConfig.VERSION_NAME} (${BuildConfig.EVE_EMAIL})")
                .build()
        } else req

        return chain.proceed(newReq)
    }
}
