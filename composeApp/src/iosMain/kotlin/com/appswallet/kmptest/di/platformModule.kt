package com.appswallet.kmptest.di

import com.appswallet.kmptest.articles.presentation.ArticlesViewModel
import org.koin.dsl.module

actual fun platformModule() = module {

    factory {
        ArticlesViewModel(get())
    }
}