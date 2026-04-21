package com.appswallet.kmptest

import android.app.Application
import com.appswallet.kmptest.di.databaseModule
import com.appswallet.kmptest.di.platformModule
import com.appswallet.kmptest.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin(){
        val modules = sharedModules + platformModule() + databaseModule()
        startKoin {
            androidContext(this@TestApp)
            modules(modules)
        }
    }
}