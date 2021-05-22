package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class StatisticResponse constructor(
        @SerializedName("statistic") val statistics: Array<StatisticEntity>
)