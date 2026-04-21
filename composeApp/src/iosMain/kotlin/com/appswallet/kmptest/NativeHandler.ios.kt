package com.appswallet.kmptest

import com.appswallet.kmptest.native.interop.kmptest_string_from_cpp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString

@OptIn(ExperimentalForeignApi::class)
actual object NativeHandler {
    actual fun getBaseUrl(): String = kmptest_string_from_cpp()?.toKString().orEmpty()
}