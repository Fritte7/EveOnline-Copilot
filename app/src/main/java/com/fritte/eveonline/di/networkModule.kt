package com.fritte.eveonline.di

import com.fritte.eveonline.BuildConfig
import com.fritte.eveonline.data.network.BearerTokenInterceptor
import com.fritte.eveonline.data.network.api.EVEEsiAPI
import com.fritte.eveonline.data.network.api.EVESsoAPI
import com.fritte.eveonline.data.network.EveTokenAuthenticator
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient

val networkModule = module {

    // EVE Login Client
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single(named("ssoRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.EVE_LOGIN_URL)
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
    }
    single<EVESsoAPI> { get<Retrofit>(named("ssoRetrofit")).create(EVESsoAPI::class.java) }

    // EVE Esi client
    single(named("esiClient")) {
        val clientId = BuildConfig.EVE_CLIENT_ID

        OkHttpClient.Builder()
            .addInterceptor(BearerTokenInterceptor(get()))
            .authenticator(EveTokenAuthenticator(get(), get(), clientId))
            .build()
    }
    single(named("esiRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.EVE_ESI_API_URL)
            .client(get<OkHttpClient>(named("esiClient")))
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
    }
    single<EVEEsiAPI> { get<Retrofit>(named("esiRetrofit")).create(EVEEsiAPI::class.java) }
}
