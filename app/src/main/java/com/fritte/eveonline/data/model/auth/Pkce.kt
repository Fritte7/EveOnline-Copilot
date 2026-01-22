package com.fritte.eveonline.data.model.auth

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

data class PkceData(
    val verifier: String,
    val challenge: String,
    val state: String,
)

object Pkce {
    private val rng = SecureRandom()
    private val urlEncoder = Base64.getUrlEncoder().withoutPadding()

    fun generate(): PkceData {
        val verifierBytes = ByteArray(32)
        rng.nextBytes(verifierBytes)

        val verifier = base64UrlNoPad(verifierBytes)
        val challenge = base64UrlNoPad(sha256(verifier.toByteArray(Charsets.US_ASCII)))
        val state = randomUrlSafeString(24)

        return PkceData(verifier = verifier, challenge = challenge, state = state)
    }

    private fun sha256(input: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(input)

    private fun base64UrlNoPad(bytes: ByteArray): String =
        urlEncoder.encodeToString(bytes)

    private fun randomUrlSafeString(len: Int): String {
        val bytes = ByteArray(len)
        rng.nextBytes(bytes)
        return base64UrlNoPad(bytes)
    }
}
