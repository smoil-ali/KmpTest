package com.appswallet.kmptest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appswallet.kmptest.articles.presentation.ArticlesViewModel
import org.jetbrains.compose.resources.painterResource

import kmptest.composeapp.generated.resources.Res
import kmptest.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin
import kotlin.coroutines.EmptyCoroutineContext.get

@Composable
@Preview
fun App() {

    val viewModel = remember {
        getKoin().get<ArticlesViewModel>()
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ArticleScreen(articlesViewModel = viewModel)
        }
    }
}