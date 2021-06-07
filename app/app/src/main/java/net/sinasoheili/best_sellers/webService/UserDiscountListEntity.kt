package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserDiscountListEntity constructor(
        @SerializedName("discount") val discount: DiscountEntity,
        @SerializedName("shop") val shop: ShopEntity
)