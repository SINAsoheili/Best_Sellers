package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopInfoEntity constructor(
    @SerializedName("find") val find: Boolean,
    @SerializedName("shop") val shop: ShopEntity
)