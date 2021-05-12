package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class LoginSellerEntity constructor(
    @SerializedName("login") val loginStatus: Boolean,
    @SerializedName("seller_id") val sellerId: Int
)