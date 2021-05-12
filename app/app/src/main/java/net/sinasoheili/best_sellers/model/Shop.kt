package net.sinasoheili.best_sellers.model

data class Shop constructor
(val id:Int
,val name: String
,val address: String
,val phone: String
,val idSeller: Int
,val idCategory:Int) {

    lateinit var description: String
    lateinit var site: String
    var latitude: Float = -1F
    var longitude: Float = -1F
}