package net.sinasoheili.best_sellers.webService

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

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
    suspend fun getShopInfo(
        @Query("shop_id") shopId: Int
    ) : ShopInfoEntity

    @GET("get_shop_badge")
    suspend fun getShopBadge (
        @Query("shop_id") shopId: Int
    ) : ShopBadgesEntity

    @GET("shop_get_message")
    suspend fun  shopGetMessage(
        @Query("shop_id") shopId: Int
    ) : ShopGetMessageResponse

    @GET("search_shop")
    suspend fun shopSearch(
        @Query("category_id") categoryId: Int,
        @Query("criteria_id") criteriaId: Int
    ) : ShopListSearchEntity

    @GET("check_user_has_shop")
    suspend fun checkUserHasShop(@Query("seller_id") sellerId: Int) : ShopInfoEntity

    @GET("get_statistic")
    suspend fun getStatistic(
        @Query("shop_id") shopId: Int
    ) : StatisticResponse

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

    @GET("register_user_message")
    suspend fun registerMessage (
            @Query("id_user") userId: Int,
            @Query("id_shop") shopId: Int,
            @Query("text") message: String
    ) : RegisterMessageEntity

    @GET("submit_question")
    suspend fun submitQuestion(
            @Query("id_user") userId: Int,
            @Query("id_shop") shopId: Int,
            @QueryMap ans: Map<String , Int>
    ) :SubmitResultEntity

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

    @GET("check_user_has_discount")
    suspend fun checkUserHasDiscount(
            @Query("shop_id") shopId: Int,
            @Query("user_id") userId: Int
    ) :CheckUserHasDiscountResponse

    @GET("get_shop_discount")
    suspend fun getShopDiscount(
            @Query("shop_id") shopId: Int
    ) : GetDiscountResponse

    @GET("user_used_discount_code")
    suspend fun deleteDiscountOfUser(
            @Query("id_shop") shopId: Int,
            @Query("id_user") userId: Int
    ) :DeleteUserDiscountResponse

    @GET("user_discount_list")
    suspend fun getUserDiscountList(
            @Query("user_id") userId: Int
    ): UserDiscountListResponse

    //Category
    @GET("get_categories_list")
    suspend fun getCategories () : CategoryEntityResponse

    @GET("get_category_criteria")
    suspend fun getCriteriaOfCategory (
            @Query("category_id") categoryId: Int
    ) : CategoryCriteriaResponse

    //Question
    @GET("user_answered_question")
    suspend fun getUserAnsweredQuestion (
            @Query("id_user") userId: Int,
            @Query("id_shop") shopId: Int
    ) : AnsweredQuestionEntityResponse

    @GET("delete_user_survey") // remove question and message of specific user of specific shop
    suspend fun removeSurvey (
            @Query("id_user") userId: Int,
            @Query("id_shop") shopId: Int
    ) : DeleteSurveyResponse
}