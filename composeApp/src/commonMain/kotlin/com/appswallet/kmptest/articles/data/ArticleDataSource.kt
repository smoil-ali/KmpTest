package com.appswallet.kmptest.articles.data

import com.appswallet.kmptest.db.KmpDb

class ArticleDataSource(private val database: KmpDb) {


    fun getAllArticles(): List<ArticleRow> =
        database.kmpTestDatabaseQueries.selectAllArticles(::mapToArticleRow).executeAsList()

    fun insert(articles: List<ArticleRow>){
        database.kmpTestDatabaseQueries.transaction{
            articles.forEach { row ->
                insertArticle(row)
            }
        }
    }

    fun clearArticles() = database.kmpTestDatabaseQueries.removeAllArticles()

    private fun insertArticle(articleRow: ArticleRow){
        database.kmpTestDatabaseQueries.insertArticle(
            articleRow.title,
            articleRow.desc,
            articleRow.date,
            articleRow.imageUrl
        )
    }
    private fun mapToArticleRow(
         title: String,
         desc: String?,
         date: String,
         url: String?
    ): ArticleRow =
        ArticleRow(
            title,
            desc,
            date,
            url
        )
    }