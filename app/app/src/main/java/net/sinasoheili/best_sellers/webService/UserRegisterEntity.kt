package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class UserRegisterEntity constructor(
        @SerializedName("status_register") val statusRegister: Boolean,
        @SerializedName("user") val user: UserEntity
)