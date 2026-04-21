package com.appswallet.kmptest.di

import com.appswallet.kmptest.articles.presentation.ArticlesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual fun platformModule() = module {

    viewModel {
        ArticlesViewModel(get())
    }
}