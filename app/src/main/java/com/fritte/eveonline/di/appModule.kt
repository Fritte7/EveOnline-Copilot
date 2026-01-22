package com.fritte.eveonline.di

import com.fritte.eveonline.data.repo.AnoikisImporterRepositoryImpl
import com.fritte.eveonline.data.repo.LocationRepositoryImpl
import com.fritte.eveonline.data.repo.VisitedSystemHistoryImporterRepositoryImpl
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.fritte.eveonline.domain.repository.LocationRepository
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImporterRepository
import com.fritte.eveonline.domain.usecase.GetLocationUIUseCase
import com.fritte.eveonline.domain.usecase.GetOnlineStatusUseCase
import com.fritte.eveonline.ui.viewmodel.AuthViewModel
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
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

    single {
        GetOnlineStatusUseCase(get())
    }

    single {
        GetLocationUIUseCase(get(), get())
    }

    viewModel {
        LocationViewModel(get(), get(), get())
    }

    viewModel {
        AuthViewModel(get(), get())
    }

    viewModel {
        StartupViewModel(get(), get())
    }

}