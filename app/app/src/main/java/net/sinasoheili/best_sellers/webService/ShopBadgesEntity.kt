package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class ShopBadgesEntity constructor(
    @SerializedName("badges") val badges: List<BadgeEntity>
)