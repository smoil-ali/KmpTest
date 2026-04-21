package com.appswallet.kmptest.di

import app.cash.sqldelight.db.SqlDriver
import com.appswallet.kmptest.db.DatabaseDriverFactory
import com.appswallet.kmptest.db.KmpDb
import org.koin.dsl.module

actual fun databaseModule() = module {
    single<SqlDriver> {
        DatabaseDriverFactory().createDriver()
    }
    single<KmpDb>{
        KmpDb(get())
    }
}