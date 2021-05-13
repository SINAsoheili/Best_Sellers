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

    //User
    @GET("register_user")
    suspend fun registerUser(
            @Query("name") name: String,
            @Query("last_name") lastName: String,
            @Query("phone") phone: String,
            @Query("passwd") passwd: String
    ) : UserRegisterEntity

    @GET("delete_user")
    suspend fun deleteUser(
            @Query("id_user") userId: Int
    ) : UserDeleteEntity

    @GET("login_user")
    suspend fun loginUser(
            @Query("phone") phone: String,
            @Query("passwd") passwd: String
    ) : UserLoginEntity

    @GET("get_user_info")
    suspend fun getUserInfo(
            @Query("user_id") userId: Int
    ) : UserInfoEntity

    @GET("check_user_ans_question")
    suspend fun checkUserAnsQuestion (
            @Query("id_user") userId: Int,
            @Query("id_shop") shopId: Int
    ) : UserAnsQuestionEntity

    @GET("get_category_question")
    suspend fun userGetQuestion(
            @Query("category_id") categoryId: Int
    ) : UserQuestionEntity

    @GET("user_get_shop_message")
    suspend fun getUserShopMessage (
            @Query("shop_id") shopId: Int,
            @Query("user_id") userId: Int
    ) : UserShopMessageEntity

    //TODO: user submit question

    //Discount
    @GET("register_discount")
    suspend fun discountRegister(
            @Query("name") name: String ,
            @Query("amount") amount: Int,
            @Query("id_shop") shopId: Int,
    ) : RegisterDiscountEntity

    @GET("delete_discount")
    suspend fun deleteDiscount (
            @Query("id_shop") shopId: Int
    ) : DiscountDeleteEntity

    //Category
    @GET("get_categories_list")
    suspend fun getCategories () : Array<CategoryEntity>

    @GET("get_category_criteria")
    suspend fun getCriteriaOfCategory (
            @Query("category_id") categoryId: Int
    ) : Array<String>
}