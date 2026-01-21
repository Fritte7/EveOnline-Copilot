package com.fritte.eveonline.data.network

import com.fritte.eveonline.BuildConfig
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor(
    private val tokenStore: DataStoreTokenRepository,
) : Interceptor {
    private val LATEST_API_COMPATIBLITY_DATE = "2025-12-16"

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = runCatching { runBlocking { tokenStore.getAccessToken() } }.getOrNull()

        val newReq = if (!token.isNullOrBlank()) {
            req.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/json")
                .header("Accept-Language", "en")
                .header("If-Modified-Since","")
                .header("If-None-Match","")
                .header("X-Compatibility-Date", LATEST_API_COMPATIBLITY_DATE)
                .header("X-Tenant", "tranquility")
                .header("User-Agent", "${BuildConfig.EVE_APP_NAME}/${BuildConfig.VERSION_NAME} (${BuildConfig.EVE_EMAIL})")
                .build()
        } else req
        return chain.proceed(newReq)
    }
}
