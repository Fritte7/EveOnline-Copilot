package com.fritte.eveonline.di

import com.fritte.eveonline.domain.usecase.GetLocationUIUseCase
import com.fritte.eveonline.domain.usecase.GetOnlineStatusUseCase
import com.fritte.eveonline.domain.usecase.RecordSystemVisitUseCase
import com.fritte.eveonline.domain.usecase.WatchSystemUseCase
import org.koin.dsl.module

val useCaseModule = module {

    factory {
        GetOnlineStatusUseCase(get())
    }

    factory {
        GetLocationUIUseCase(get(), get(), get())
    }

    factory {
        RecordSystemVisitUseCase(get(), get())
    }

    factory {
        WatchSystemUseCase(get())
    }
}