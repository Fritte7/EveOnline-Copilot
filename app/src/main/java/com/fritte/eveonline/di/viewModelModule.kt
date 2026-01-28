package com.fritte.eveonline.di

import com.fritte.eveonline.ui.viewmodel.AuthViewModel
import com.fritte.eveonline.ui.viewmodel.LocationViewModel
import com.fritte.eveonline.ui.viewmodel.NavigationViewModel
import com.fritte.eveonline.ui.viewmodel.StartupViewModel
import com.fritte.eveonline.ui.viewmodel.TimelineViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        LocationViewModel(get(), get(), get())
    }

    viewModel {
        AuthViewModel(get(), get())
    }

    viewModel {
        StartupViewModel(get(), get())
    }

    viewModel {
        TimelineViewModel(get())
    }

    viewModel {
        NavigationViewModel()
    }
}