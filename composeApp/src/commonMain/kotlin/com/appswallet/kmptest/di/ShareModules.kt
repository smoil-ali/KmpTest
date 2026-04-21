package com.appswallet.kmptest.di

import com.appswallet.kmptest.articles.di.articleModule

val sharedModules = listOf(
    articleModule,
    networkModule
)