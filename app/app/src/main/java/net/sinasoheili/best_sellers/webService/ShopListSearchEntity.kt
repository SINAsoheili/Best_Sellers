package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopListSearchEntity constructor(
    @SerializedName("shop_list") val shopList: List<ShopEntity>
)