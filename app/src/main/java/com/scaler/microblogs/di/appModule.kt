package com.scaler.microblogs.di

import com.scaler.libconduit.ConduitClient
import com.scaler.libconduit.apis.ConduitApi
import com.scaler.microblogs.data.AppRepositoryImpl
import com.scaler.microblogs.data.AuthRepositoryImpl
import com.scaler.microblogs.ui.account.AccountViewModel
import com.scaler.microblogs.ui.feed.FeedViewModel
import com.scaler.microblogs.ui.tags.TagsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {

    fun provideApi(): ConduitApi {

        val client = ConduitClient()
        return client.retrofit.create(ConduitApi::class.java)
    }

    single { provideApi() }
}

val authRepositoryImplModule = module {

    single { AuthRepositoryImpl(get()) }
}

val appRepositoryImplModule = module {

    single { AppRepositoryImpl(get()) }
}

val feedViewModel = module {

    viewModel { FeedViewModel(get()) }
}

val tagsViewModel = module {

    viewModel { TagsViewModel(get()) }
}

val accountViewModel = module {

    viewModel { AccountViewModel(get()) }
}