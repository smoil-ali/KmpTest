package com.appswallet.kmptest

import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


