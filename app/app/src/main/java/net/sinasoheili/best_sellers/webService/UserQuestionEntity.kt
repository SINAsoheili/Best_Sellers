package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserQuestionEntity constructor(
        @SerializedName("find") val findQuestion: Boolean,
        @SerializedName("questions") val questions: List<QuestionEntity>
)