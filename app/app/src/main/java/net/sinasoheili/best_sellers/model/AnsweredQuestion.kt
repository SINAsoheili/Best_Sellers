package net.sinasoheili.best_sellers.model

data class AnsweredQuestion constructor(
        val content: String,
        val questionId: Int,
        val score: Int,
)