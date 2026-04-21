package com.appswallet.kmptest

import com.appswallet.kmptest.di.databaseModule
import com.appswallet.kmptest.di.platformModule
import com.appswallet.kmptest.di.sharedModules
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(sharedModules + platformModule() + databaseModule())
    }
}