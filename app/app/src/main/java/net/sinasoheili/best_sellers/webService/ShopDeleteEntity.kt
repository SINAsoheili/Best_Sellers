package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopDeleteEntity constructor(
    @SerializedName("shop_deleted") val shopDeleted: Boolean
)