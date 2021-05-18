package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class RegisterShopEntity constructor(
    @SerializedName("shop") val shop: ShopEntity ,
    @SerializedName("status_register") val statusRegister: Boolean
)