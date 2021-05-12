package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class BadgeEntity constructor(
    @SerializedName("id") var id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("category") var category: String,
    @SerializedName("min_score") val minScore: Int
)