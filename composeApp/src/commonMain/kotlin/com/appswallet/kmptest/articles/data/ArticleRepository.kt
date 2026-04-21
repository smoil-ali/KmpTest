package com.appswallet.kmptest.articles.data

import com.appswallet.kmptest.articles.data.ArticleService

class ArticleRepository(
    private val dataSource: ArticleDataSource,
    private val service: ArticleService
) {
    suspend fun getArticles(forceRefresh: Boolean): List<ArticleRow>{

        if (forceRefresh){
            dataSource.clearArticles()
            return fetchArticles()
        }
        val articlesDb = dataSource.getAllArticles()

        if (articlesDb.isEmpty()){
            return fetchArticles()
        }

        return articlesDb
    }

    private suspend fun fetchArticles(): List<ArticleRow>{
        val fetchArticles = service.fetchArticles()
        dataSource.insert(fetchArticles)
        return fetchArticles
    }
}