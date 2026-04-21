package com.appswallet.kmptest.articles.presentation

import com.appswallet.kmptest.articles.domain.Article

data class ArticleState(
    val articles: List<Article> = listOf(),
    val loading: Boolean = false,
    val error: String? = null
)