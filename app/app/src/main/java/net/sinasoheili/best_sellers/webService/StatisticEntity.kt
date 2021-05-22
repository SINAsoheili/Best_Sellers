package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class StatisticEntity constructor(
        @SerializedName("name") val name: String,
        @SerializedName("amount") val amount: Float
)