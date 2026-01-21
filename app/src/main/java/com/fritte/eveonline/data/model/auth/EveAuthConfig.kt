package com.fritte.eveonline.data.model.auth

import android.net.Uri
import com.fritte.eveonline.BuildConfig
import androidx.core.net.toUri

data class EveAuthConfig(
    val clientId: String,
    val redirectUri: String,
    val scopes: List<String>, // e.g. listOf("esi-location.read_location.v1")
)

fun buildAuthorizeUri(cfg: EveAuthConfig, pkce: PkceData): Uri {
    val scopeStr = cfg.scopes.joinToString(" ")

    return "${BuildConfig.EVE_LOGIN_URL}v2/oauth/authorize/".toUri().buildUpon()
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("redirect_uri", cfg.redirectUri)
        .appendQueryParameter("client_id", cfg.clientId)
        .appendQueryParameter("scope", scopeStr)
        .appendQueryParameter("code_challenge", pkce.challenge)
        .appendQueryParameter("code_challenge_method", "S256")
        .appendQueryParameter("state", pkce.state)
        .build()
}

