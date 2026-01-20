package com.fritte.eveonline.di

import androidx.room.Room
import com.fritte.eveonline.data.room.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "eveonline_copilot.db"
        )
        .fallbackToDestructiveMigration(false)
        .build()
    }

    single { get<AppDatabase>().systemDao() }
    single { get<AppDatabase>().regionDao() }
    single { get<AppDatabase>().constellationDao() }
    single { get<AppDatabase>().visitedSystemDao() }
}