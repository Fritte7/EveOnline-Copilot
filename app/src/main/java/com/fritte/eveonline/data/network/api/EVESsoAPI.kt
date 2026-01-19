package com.fritte.eveonline.data.network.api
import com.fritte.eveonline.data.model.eve.OAuthTokenResponse
import retrofit2.http.*

interface EVESsoAPI {
    @FormUrlEncoded
    @POST("v2/oauth/token")
    suspend fun requestToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("redirect_uri") redirectUri: String,
    ): OAuthTokenResponse

    @FormUrlEncoded
    @POST("v2/oauth/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
    ): OAuthTokenResponse
}
