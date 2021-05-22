package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopGetMessageResponse constructor(
    @SerializedName("find") val find: Boolean,
    @SerializedName("messages") val messages: List<String>
)