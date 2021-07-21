package net.sinasoheili.best_sellers.model

data class Shop constructor
    ( var name: String,
      var address: String,
      var idSeller: Int = -1,
      var idCategory:Int = -1,
      var phone: String = "",
      var description: String = "",
      var site: String = "",
      var latitude: Float = -1F,
      var longitude: Float = -1F,
      var id:Int = -1,
      var city: String = ""
    )