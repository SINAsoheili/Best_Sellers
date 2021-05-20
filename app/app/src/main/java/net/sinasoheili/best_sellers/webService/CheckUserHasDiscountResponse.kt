package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class CheckUserHasDiscountResponse constructor(
        @SerializedName("discount_enable") val discountAvailable: Boolean
)