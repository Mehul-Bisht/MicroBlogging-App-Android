package com.scaler.microblogs.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TheApplication)
            modules(
                listOf(
                    authModule,
                    apiModule,
                    authRepositoryImplModule,
                    appRepositoryImplModule,
                    feedViewModel,
                    tagsViewModel,
                    accountViewModel
                )
            )
        }
    }
}