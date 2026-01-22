package com.fritte.eveonline.data.model.auth

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class EveAuthConfig(
    val clientId: String,
    val redirectUri: String,
    val scopes: List<String>,
    val loginBaseUrl: String, // inject instead of BuildConfig
)

fun buildAuthorizeUrl(cfg: EveAuthConfig, pkce: PkceData): String {
    val scopeStr = cfg.scopes.joinToString(" ")

    fun enc(s: String) = URLEncoder.encode(s, StandardCharsets.UTF_8.toString())

    val base = cfg.loginBaseUrl + "v2/oauth/authorize/"

    // deterministic ordering helps tests
    val query = listOf(
        "response_type" to "code",
        "redirect_uri" to cfg.redirectUri,
        "client_id" to cfg.clientId,
        "scope" to scopeStr,
        "code_challenge" to pkce.challenge,
        "code_challenge_method" to "S256",
        "state" to pkce.state,
    ).joinToString("&") { (k, v) -> "${enc(k)}=${enc(v)}" }

    return "$base?$query"
}