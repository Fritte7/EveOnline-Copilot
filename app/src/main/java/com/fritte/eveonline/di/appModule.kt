package com.fritte.eveonline.di

import com.fritte.eveonline.data.repo.AnoikisImporterRepositoryImpl
import com.fritte.eveonline.data.repo.DataStoreTokenRepositoryImpl
import com.fritte.eveonline.data.repo.LocationRepositoryImpl
import com.fritte.eveonline.data.repo.SystemRepositoryImpl
import com.fritte.eveonline.data.repo.VisitedSystemHistoryImporterRepositoryImpl
import com.fritte.eveonline.data.repo.VisitedSystemRepositoryImpl
import com.fritte.eveonline.data.repo.WatchedSystemRepositoryImpl
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.fritte.eveonline.domain.repository.DataStoreTokenRepository
import com.fritte.eveonline.domain.repository.LocationRepository
import com.fritte.eveonline.domain.repository.SystemRepository
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImporterRepository
import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import com.fritte.eveonline.domain.repository.WatchedSystemRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<DataStoreTokenRepository> {
        DataStoreTokenRepositoryImpl(androidContext())
    }

    single<AnoikisImporterRepository> {
        AnoikisImporterRepositoryImpl(androidContext(), get(), get())
    }

    single<VisitedSystemHistoryImporterRepository> {
        VisitedSystemHistoryImporterRepositoryImpl(androidContext(), get(), get())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(get())
    }

    single<SystemRepository> {
        SystemRepositoryImpl(get())
    }

    single<VisitedSystemRepository> {
        VisitedSystemRepositoryImpl(get())
    }

    single<WatchedSystemRepository> {
        WatchedSystemRepositoryImpl(get())
    }
}