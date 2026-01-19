package com.fritte.eveonline.utils
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

data class PkceData(
    val verifier: String,
    val challenge: String,
    val state: String,
)

object Pkce {
    private val rng = SecureRandom()

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
        Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)

    private fun randomUrlSafeString(len: Int): String {
        val bytes = ByteArray(len)
        rng.nextBytes(bytes)
        return base64UrlNoPad(bytes)
    }
}
