package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class GetDiscountResponse constructor(
        @SerializedName("discount") val discount: DiscountEntity,
        @SerializedName("find") val find: Boolean,
)