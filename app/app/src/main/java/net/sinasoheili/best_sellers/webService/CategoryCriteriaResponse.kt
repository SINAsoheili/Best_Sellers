package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class CategoryCriteriaResponse constructor(
        @SerializedName("criterias") val criterias: Array<CriteriaEntity>
)