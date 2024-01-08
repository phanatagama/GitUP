package com.github.gituser.ui.common

import com.github.gituser.ui.main.MainViewModel
import com.github.gituser.ui.main.detail.DetailViewModel
import com.github.gituser.ui.main.detail.FollowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { DetailViewModel(get(), get(), get(), get()) }
    viewModel { FollowViewModel(get(), get()) }
}