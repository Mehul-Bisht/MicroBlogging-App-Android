package com.scaler.microblogs.di

import com.scaler.microblogs.ui.account.AccountViewModel
import com.scaler.microblogs.ui.feed.FeedViewModel
import com.scaler.microblogs.ui.tags.TagsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val feedViewModel = module {

    viewModel { FeedViewModel() }
}

val tagsViewModel = module {

    viewModel { TagsViewModel() }
}

val accountViewModel = module {

    viewModel { AccountViewModel() }
}