package com.appswallet.kmptest.articles.domain

import com.appswallet.kmptest.articles.data.ArticleRepository
import com.appswallet.kmptest.articles.data.ArticleRow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.Instant

class ArticlesUseCase(
    private val repo: ArticleRepository
) {
    suspend fun getArticles(forceRefresh: Boolean): List<Article>{
        val articleRows = repo.getArticles(forceRefresh)
        return mapArticles(articleRows)
    }

    private fun mapArticles(articlesRow: List<ArticleRow>): List<Article> =
        articlesRow.map { row ->
            Article(
                row.title,
                row.desc ?: "Click to find more",
                getDaysAgoString(row.date),
                row.imageUrl ?: ""
            )
    }

    private fun getDaysAgoString(date: String): String{
        val today = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault())
        val days = today.daysUntil(
            Instant.parse(date).toLocalDateTime(TimeZone.Companion.currentSystemDefault()).date
        )

        val result = when {
            abs(days) > 1 -> "${abs(days)} days ago"
            abs(days) == 1 -> "Yesterday"
            else -> "Today"
        }

        return result
    }
}