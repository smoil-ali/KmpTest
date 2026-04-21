package com.appswallet.kmptest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.appswallet.kmptest.articles.domain.Article
import com.appswallet.kmptest.articles.presentation.ArticlesViewModel
import org.koin.compose.viewmodel.koinViewModel



@Composable
fun ArticleScreen(
    articlesViewModel: ArticlesViewModel
){

    val articleState = articlesViewModel.articlesState.collectAsState()

    Column {

        TopBar(articlesViewModel.title)
        if (articleState.value.error != null){
            ErrorMessage(articleState.value.error!!)
        }
        if (articleState.value.articles.isNotEmpty()){
            ArticlesListView(articlesViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String){


    TopAppBar(
        title = { Text(text = title) }
    )

}

@Composable
fun Loader(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = Color.Red,
            trackColor = Color.Black
        )
    }
}

@Composable
fun ErrorMessage(message: String){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = message,
            style = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun ArticlesListView(viewModel: ArticlesViewModel){

    PullToRefreshBox(onRefresh = {viewModel.getArticle(true)},
        isRefreshing = viewModel.articlesState.value.loading,
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.articlesState.value.articles){ article ->
                    ArticleItem(article)
                }
            }
        }
    )

}

@Composable
fun ArticleItem(article: Article){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        AsyncImage(
            model = article.imageUrl,
            contentDescription = null,
            onError = {
                print("loading articles ${it.result}")
            },
            onLoading = {
            },
            onSuccess = {
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.title,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.description,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.date,
            style = TextStyle(color = Color.Gray),
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(4.dp))

    }
}