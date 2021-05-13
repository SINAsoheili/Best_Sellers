package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserDeleteEntity constructor(
        @SerializedName("user_deleted") val userDeleted: Boolean
)