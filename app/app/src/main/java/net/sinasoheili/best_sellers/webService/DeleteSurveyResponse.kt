package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class DeleteSurveyResponse constructor(
        @SerializedName("result") val result: Boolean
)