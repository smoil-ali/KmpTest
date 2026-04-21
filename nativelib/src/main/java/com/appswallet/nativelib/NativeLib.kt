package com.appswallet.nativelib

class NativeLib {


    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'nativelib' library on application startup.
        init {
            System.loadLibrary("nativelib")
        }
    }
}