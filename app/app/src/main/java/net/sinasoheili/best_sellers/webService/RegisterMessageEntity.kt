package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class RegisterMessageEntity constructor(
        @SerializedName("message") val message: MessageEntity,
        @SerializedName("message_register") val messageRegister: Boolean
)