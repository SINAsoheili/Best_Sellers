package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class DeleteSellerEntity constructor(
    @SerializedName("seller_deleted")  val isDelete: Boolean
)