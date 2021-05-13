package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class RegisterDiscountEntity constructor(
        @SerializedName("status_register") val statusRegister: Boolean,
        @SerializedName("discount") val discount: DiscountEntity
)