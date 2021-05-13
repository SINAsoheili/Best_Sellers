package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserInfoEntity constructor(
        @SerializedName("find") val findStatus: Boolean,
        @SerializedName("user") val user: UserEntity
)