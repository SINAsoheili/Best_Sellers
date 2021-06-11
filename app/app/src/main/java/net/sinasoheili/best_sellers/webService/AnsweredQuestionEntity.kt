package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class AnsweredQuestionEntity constructor(
        @SerializedName("content") val content: String,
        @SerializedName("id_question") val questionId: Int,
        @SerializedName("score") val score: Int,
)