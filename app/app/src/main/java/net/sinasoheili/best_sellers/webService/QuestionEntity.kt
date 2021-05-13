package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class QuestionEntity constructor(
        @SerializedName("content") val text: String ,
        @SerializedName("id_question") val questionId: Int
)