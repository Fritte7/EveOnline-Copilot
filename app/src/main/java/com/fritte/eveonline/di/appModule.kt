package com.fritte.eveonline.di

import com.fritte.eveonline.data.AnoikisImporter
import com.fritte.eveonline.data.repo.LocationRepositoryImpl
import com.fritte.eveonline.ui.auth.AuthViewModel
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        AnoikisImporter(get(), get(), get())
    }

    single {
        LocationRepositoryImpl(get())
    }

    viewModel {
        LocationViewModel(get())
    }

    viewModel {
        AuthViewModel(get(), get())
    }

    viewModel {
        StartupViewModel(get())
    }
}