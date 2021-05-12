package net.sinasoheili.best_sellers.webService

import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    //Seller
    @GET("register_seller")
    suspend fun registerSeller(
        @Query("name") name: String,
        @Query("last_name") lastName: String,
        @Query("phone") phone: String,
        @Query("passwd") passwd: String
    ) : RegisterSellerEntity

    @GET("delete_seller")
    suspend fun deleteSeller(
        @Query("id_seller") idSeller: Int
    ) : DeleteSellerEntity

    @GET("login_seller")
    suspend fun loginSeller(
        @Query("phone") phone: String,
        @Query("passwd") passwd: String
    ) : LoginSellerEntity

    @GET("get_seller_info")
    suspend fun getSellerInfo(
        @Query("seller_id") sellerId: Int
    ) : SellerInfoEntity
    
}