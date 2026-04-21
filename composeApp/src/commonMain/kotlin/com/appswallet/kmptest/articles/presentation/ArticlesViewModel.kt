package com.appswallet.kmptest.articles.presentation

import com.appswallet.kmptest.BaseViewModel
import com.appswallet.kmptest.NativeHandler
import com.appswallet.kmptest.articles.domain.ArticlesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticlesViewModel(
    private val articlesUseCase: ArticlesUseCase
): BaseViewModel() {

    private val _articlesState = MutableStateFlow(ArticleState(loading = true))
    val articlesState: StateFlow<ArticleState> get()  = _articlesState

    val title = NativeHandler.getBaseUrl()

    init {
        getArticle()
    }
    fun getArticle(forceRefresh: Boolean = false){
        scope.launch {
            _articlesState.emit(ArticleState(loading = true))
            val fetchArticles = articlesUseCase.getArticles(forceRefresh)
            _articlesState.emit(ArticleState(articles = fetchArticles))
        }
    }
}