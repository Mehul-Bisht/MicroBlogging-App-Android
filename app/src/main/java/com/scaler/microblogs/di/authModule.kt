package com.scaler.microblogs.di

import com.scaler.microblogs.ui.auth.AuthViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {

    viewModel { AuthViewModel(get()) }
}