package com.appswallet.kmptest

import androidx.compose.ui.window.ComposeUIViewController


fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}