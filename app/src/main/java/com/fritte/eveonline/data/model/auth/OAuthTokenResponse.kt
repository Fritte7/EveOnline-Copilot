package com.fritte.eveonline.data.model.auth

data class OAuthTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val refresh_token: String? = null
)