package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class AnsweredQuestionEntityResponse constructor(
        @SerializedName("result") val result: List<AnsweredQuestionEntity>
)