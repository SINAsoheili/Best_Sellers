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

    //Shop
    @GET("register_shop")
    suspend fun registerShop(
        @Query("name") name: String,
        @Query("address") address: String,
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("id_seller") sellerId: Int,
        @Query("id_category") categoryId: Int
    ) : RegisterShopEntity

    @GET("delete_shop")
    suspend fun deleteShop(
        @Query("id_shop") shopId: Int
    ) : ShopDeleteEntity

    @GET("get_shop_info")
    suspend fun getShopInto(
        @Query("shop_id") shopId: Int
    ) : ShopInfoEntity

    @GET("get_shop_badge")
    suspend fun getShopBadge (
        @Query("shop_id") shopId: Int
    ) : ShopBadgesEntity

    @GET("shop_get_message")
    suspend fun  shopGetMessage(
        @Query("shop_id") shopId: Int
    ) : ShopGetMessageEntity

    @GET("search_shop")
    suspend fun shopSearch(
        @Query("category_id") categoryId: Int,
        @Query("criteria") criteria: String
    ) : ShopListSearchEntity
}