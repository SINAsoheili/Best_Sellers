package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class DiscountDeleteEntity constructor(
        @SerializedName("discount_deleted") val deleteStatus: Boolean
)