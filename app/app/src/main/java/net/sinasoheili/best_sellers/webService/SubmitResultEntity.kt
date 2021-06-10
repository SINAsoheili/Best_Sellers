package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class SubmitResultEntity constructor(
        @SerializedName("registered") val registered: Boolean
)