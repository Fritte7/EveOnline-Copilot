package com.fritte.eveonline.di

import com.fritte.eveonline.data.repo.AnoikisImporterRepositoryImpl
import com.fritte.eveonline.data.repo.LocationRepositoryImpl
import com.fritte.eveonline.data.repo.VisitedSystemHistoryImporterRepositoryImpl
import com.fritte.eveonline.data.repo.VisitedSystemRepositoryImpl
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.fritte.eveonline.domain.repository.LocationRepository
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImporterRepository
import com.fritte.eveonline.domain.repository.VisitedSystemRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<AnoikisImporterRepository> {
        AnoikisImporterRepositoryImpl(androidContext(), get(), get())
    }

    single<VisitedSystemHistoryImporterRepository> {
        VisitedSystemHistoryImporterRepositoryImpl(androidContext(), get(), get())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(get())
    }

    single<VisitedSystemRepository> {
        VisitedSystemRepositoryImpl(get())
    }
}