package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopEntity constructor (
    @SerializedName("id")  val id:Int,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("id_seller") val idSeller: Int,
    @SerializedName("id_category") val idCategory:Int,
    @SerializedName("description") var description: String?,
    @SerializedName("site") var site: String?,
    @SerializedName("latitude") var latitude: Float,
    @SerializedName("longitude") var longitude: Float,
)