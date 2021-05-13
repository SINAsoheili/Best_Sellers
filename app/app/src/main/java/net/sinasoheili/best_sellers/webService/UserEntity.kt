package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserEntity constructor(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("phone") val phone: String,
)