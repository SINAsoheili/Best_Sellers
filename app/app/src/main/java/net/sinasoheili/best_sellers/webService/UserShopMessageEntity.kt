package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserShopMessageEntity constructor(
        @SerializedName("find") val findMessage: Boolean,
        @SerializedName("message") val message: MessageEntity
)