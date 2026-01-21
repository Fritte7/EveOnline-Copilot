package com.fritte.eveonline.data.model.auth

data class EveJwtClaims(
    val sub: String,
    val name: String?,
    val exp: Long?,
    val iss: String?,
)