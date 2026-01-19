package com.fritte.eveonline

import android.app.Application
import com.fritte.eveonline.di.appModule
import com.fritte.eveonline.di.authModule
import com.fritte.eveonline.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(
                appModule,
                networkModule,
                authModule
            )
        }
    }
}