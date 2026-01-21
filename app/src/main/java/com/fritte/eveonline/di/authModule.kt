package com.fritte.eveonline.di

import com.fritte.eveonline.BuildConfig
import com.fritte.eveonline.data.model.auth.EveAuthConfig
import com.fritte.eveonline.data.model.auth.EveAuthManager
import org.koin.dsl.module

// TODO add any esi- scopes permission
val authModule = module {

    single {
        EveAuthConfig(
            clientId = BuildConfig.EVE_CLIENT_ID,
            redirectUri = BuildConfig.EVE_CALLBACK_URL,
            scopes = listOf(
                "esi-location.read_location.v1",
                "esi-location.read_ship_type.v1",
                "esi-location.read_online.v1",
            ),
        )
    }

    single {
        EveAuthManager(get(), get(), get(), get())
    }
}
