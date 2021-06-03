package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class CriteriaEntity constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
        )