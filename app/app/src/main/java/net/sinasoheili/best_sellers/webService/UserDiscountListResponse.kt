package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserDiscountListResponse constructor(
        @SerializedName("discounts") val discounts:ArrayList<UserDiscountListEntity>
        )