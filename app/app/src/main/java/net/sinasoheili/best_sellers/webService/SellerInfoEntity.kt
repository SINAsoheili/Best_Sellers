package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class SellerInfoEntity constructor(
    @SerializedName("find") val findStatus: Boolean,
    @SerializedName("seller") val seller: SellerEntity

)