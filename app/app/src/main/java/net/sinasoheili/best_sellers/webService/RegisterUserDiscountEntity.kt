package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class RegisterUserDiscountEntity (
        @SerializedName("discount_register") val status: Boolean
        )