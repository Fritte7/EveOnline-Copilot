package com.fritte.eveonline

import com.fritte.eveonline.data.model.auth.EveAuthConfig
import com.fritte.eveonline.data.model.auth.EveAuthManager
import com.fritte.eveonline.data.model.auth.OAuthTokenResponse
import com.fritte.eveonline.data.network.api.EVESsoAPI
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.coJustRun
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Base64

class EveAuthManagerTest {
    private val ssoApi: EVESsoAPI = mockk()
    private val tokenStore: DataStoreTokenRepository = mockk()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val cfg = EveAuthConfig(
        clientId = BuildConfig.EVE_CLIENT_ID,
        redirectUri = BuildConfig.EVE_CALLBACK_URL,
        scopes = listOf(
            "esi-location.read_location.v1",
            "esi-location.read_ship_type.v1",
            "esi-location.read_online.v1",
        ),
        "https://login.eveonline.com/"
    )

    private fun makeSut(): EveAuthManager =
        EveAuthManager(
            cfg = cfg,
            ssoApi = ssoApi,
            tokenStore = tokenStore,
            moshi = moshi
        )

    @Test
    fun `startLogin returns authorize uri containing state`() {
        val sut = makeSut()
        val state = URI(sut.startLogin())
            .rawQuery
            ?.split("&")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it[0] == "state" }
            ?.getOrNull(1)

        assertFalse(state.isNullOrBlank(), "state should be present in authorize URI")
    }

    @Test
    fun `handleAuthCode returns false when state mismatch and does not call api`() = runTest {
        val sut = makeSut()
        val state = URI(sut.startLogin())
            .rawQuery
            ?.split("&")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it[0] == "state" }
            ?.getOrNull(1)
        val correctState = requireNotNull(state)

        val ok = sut.handleAuthCode(code = "auth-code", returnedState = correctState + "_tampered")

        assertFalse(ok)
        coVerify(exactly = 0) { ssoApi.requestToken(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { tokenStore.saveTokens(any(), any()) }
        coVerify(exactly = 0) { tokenStore.saveSession(any(), any()) }
        confirmVerified(ssoApi, tokenStore)
    }

    @Test
    fun `handleAuthCode success calls api parses jwt and stores tokens and session then clears pending`() = runTest {
        val sut = makeSut()
        val state = URI(sut.startLogin())
            .rawQuery
            ?.split("&")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it[0] == "state" }
            ?.getOrNull(1)
        val correctState = requireNotNull(state)

        val jwt = makeJwt(
            payloadJson = """
                {"sub":"CHARACTER:EVE:123456789","name":"Fritte Cornelius"}
            """.trimIndent()
        )

        val tokenResponse = OAuthTokenResponse(
            access_token = jwt,
            token_type = "type",
            refresh_token = "refresh-xyz",
            expires_in = 5000
        )

        coEvery {
            ssoApi.requestToken(
                code = "auth-code",
                clientId = cfg.clientId,
                codeVerifier = any(),
                redirectUri = cfg.redirectUri
            )
        } returns tokenResponse

        coJustRun { tokenStore.saveTokens(any(), any()) }
        coJustRun { tokenStore.saveSession(any(), any()) }

        val ok = sut.handleAuthCode(code = "auth-code", returnedState = correctState)
        assertTrue(ok)

        coVerify(exactly = 1) { tokenStore.saveTokens(jwt, "refresh-xyz") }
        coVerify(exactly = 1) { tokenStore.saveSession(123456789L, "Fritte Cornelius") }

        val second = sut.handleAuthCode(code = "auth-code-2", returnedState = correctState)
        assertFalse(second)

        coVerify(exactly = 1) { ssoApi.requestToken(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `handleAuthCode returns false if called before startLogin`() = runTest {
        val sut = makeSut()

        val ok = sut.handleAuthCode(code = "auth-code", returnedState = "state")
        assertFalse(ok)

        coVerify(exactly = 0) { ssoApi.requestToken(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { tokenStore.saveTokens(any(), any()) }
        coVerify(exactly = 0) { tokenStore.saveSession(any(), any()) }
    }

    @Test
    fun `handleAuthCode returns false when jwt is invalid and does not store`() = runTest {
        val sut = makeSut()
        val state = URI(sut.startLogin())
            .rawQuery
            ?.split("&")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it[0] == "state" }
            ?.getOrNull(1)
        val correctState = requireNotNull(state)

        val tokenResponse = OAuthTokenResponse(
            access_token = "not-a-jwt",
            refresh_token = "refresh-xyz",
            token_type = "type",
            expires_in = 5000
        )

        coEvery { ssoApi.requestToken(any(), any(), any(), any(), any()) } returns tokenResponse

        val ok = sut.handleAuthCode("auth-code", correctState)
        assertFalse(ok)

        coVerify(exactly = 0) { tokenStore.saveTokens(any(), any()) }
        coVerify(exactly = 0) { tokenStore.saveSession(any(), any()) }
    }

    @Test
    fun `concurrency two callbacks only one succeeds and api called once`() = runTest {
        val sut = makeSut()
        val state = URI(sut.startLogin())
            .rawQuery
            ?.split("&")
            ?.map { it.split("=", limit = 2) }
            ?.firstOrNull { it[0] == "state" }
            ?.getOrNull(1)
        val correctState = requireNotNull(state)

        val jwt = makeJwt("""{"sub":"CHARACTER:EVE:42","name":"Pilot"}""")

        val tokenResponse = OAuthTokenResponse(
            access_token = jwt,
            refresh_token = "refresh",
            token_type = "type",
            expires_in = 5000
        )

        coEvery { ssoApi.requestToken(any(), any(), any(), any(), any()) } returns tokenResponse
        coJustRun { tokenStore.saveTokens(any(), any()) }
        coJustRun { tokenStore.saveSession(any(), any()) }

        val a = async { sut.handleAuthCode("codeA", correctState) }
        val b = async { sut.handleAuthCode("codeB", correctState) }

        val r1 = a.await()
        val r2 = b.await()

        assertTrue(r1 xor r2)

        coVerify(exactly = 1) { ssoApi.requestToken(any(), any(), any(), any(), any()) }
        coVerify(exactly = 1) { tokenStore.saveTokens(any(), any()) }
        coVerify(exactly = 1) { tokenStore.saveSession(any(), any()) }
    }

    private fun makeJwt(payloadJson: String): String {
        val header = """{"alg":"none","typ":"JWT"}"""
        val headerB64 = b64Url(header.toByteArray(StandardCharsets.UTF_8))
        val payloadB64 = b64Url(payloadJson.toByteArray(StandardCharsets.UTF_8))
        return "$headerB64.$payloadB64."
    }

    private fun b64Url(bytes: ByteArray): String =
        Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}