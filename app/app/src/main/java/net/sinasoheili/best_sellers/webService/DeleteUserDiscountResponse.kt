package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class DeleteUserDiscountResponse constructor(
        @SerializedName("user_discount_deleted") val deleteStatus: Boolean
        )