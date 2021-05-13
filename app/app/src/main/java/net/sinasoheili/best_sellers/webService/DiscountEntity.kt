package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class DiscountEntity constructor(
        @SerializedName("name") val name: String,
        @SerializedName("amount") val amount: Int,
        @SerializedName("id_shop") val shopId: Int
)