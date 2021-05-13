package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class MessageEntity constructor(
        @SerializedName("id_shop") val shopId: Int,
        @SerializedName("id_user") val userId: Int,
        @SerializedName("text") val text: String
)