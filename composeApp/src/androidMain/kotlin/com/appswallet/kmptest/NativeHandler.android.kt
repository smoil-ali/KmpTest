package com.appswallet.kmptest

import com.appswallet.nativelib.NativeLib

actual object NativeHandler {
    actual fun getBaseUrl(): String = NativeLib().stringFromJNI()
}