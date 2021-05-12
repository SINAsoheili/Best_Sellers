package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopSearchEntity constructor(
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("shop_name") val shopName: String
)