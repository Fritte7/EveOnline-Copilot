package com.fritte.eveonline.di

import com.fritte.eveonline.data.repo.LocationRepositoryImpl
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        LocationRepositoryImpl(get())
    }

    viewModel {
        LocationViewModel(get())
    }
}