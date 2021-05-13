package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserAnsQuestionEntity constructor(
        @SerializedName("answered") val userAnsStatus: Boolean
)