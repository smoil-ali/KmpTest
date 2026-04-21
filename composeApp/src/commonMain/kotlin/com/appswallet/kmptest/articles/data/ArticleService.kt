package com.appswallet.kmptest.articles.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ArticleService(
    private val client: HttpClient
) {

    private val country = "us"
    private val category = "business"
    private val apiKey = "3a50c892b703428b8d7bd3f330790e7b"
    private val baseUrl = "https://newsapi.org/v2"

    suspend fun fetchArticles(): List<ArticleRow>{
        val response: ArticleResponse = client.get("${baseUrl}/top-headlines?country=$country&category=$category&apiKey=$apiKey").body()
        return response.articles
    }
}