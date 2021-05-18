package net.sinasoheili.best_sellers.webService

import com.google.gson.annotations.SerializedName

data class CategoryEntityResponse constructor( @SerializedName("categories") val arr: Array<CategoryEntity>)