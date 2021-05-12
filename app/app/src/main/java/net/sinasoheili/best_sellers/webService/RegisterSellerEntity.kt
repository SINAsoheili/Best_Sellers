package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class RegisterSellerEntity constructor(
    @SerializedName("seller")  val seller: SellerEntity,
    @SerializedName("status_register")  val isRegister: Boolean
)