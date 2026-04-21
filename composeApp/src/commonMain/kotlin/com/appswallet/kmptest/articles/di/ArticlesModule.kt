package com.appswallet.kmptest.articles.di

import com.appswallet.kmptest.articles.data.ArticleDataSource
import com.appswallet.kmptest.articles.data.ArticleRepository
import com.appswallet.kmptest.articles.data.ArticleService
import com.appswallet.kmptest.articles.domain.ArticlesUseCase
import com.appswallet.kmptest.articles.presentation.ArticlesViewModel
import org.koin.dsl.module
import kotlin.math.sin

val articleModule = module {

    single<ArticleService>{
        ArticleService(get())
    }
    single<ArticlesUseCase>{
        ArticlesUseCase(get())
    }
    single<ArticlesViewModel> { ArticlesViewModel(get()) }

    single<ArticleDataSource> { ArticleDataSource(get()) }
    single<ArticleRepository> { ArticleRepository(get(),get()) }


}