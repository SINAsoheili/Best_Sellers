package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserLoginEntity constructor(
        @SerializedName("login") val loginStatus: Boolean,
        @SerializedName("user_id") val userId: Int
)