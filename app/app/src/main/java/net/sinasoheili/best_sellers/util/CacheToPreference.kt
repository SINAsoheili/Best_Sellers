package net.sinasoheili.best_sellers.util

import android.content.Context
import com.google.gson.Gson
import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.User

object CacheToPreference {

    private const val PREF_NAME = "LOGIN_PREFERENCES"

    fun setWhoLogIn(context: Context, who: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString("who" , who)
            .apply()
    }

    fun getWhoLogIn(context: Context) : String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString("who" , null)
    }

    fun deleteWhoFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("who")
                .apply()
    }


    fun setIdPerson(context: Context, id: Int) {
        context.getSharedPreferences(PREF_NAME , Context.MODE_PRIVATE)
            .edit()
            .putInt("id" , id)
            .apply()
    }

    fun getPersonId(context: Context) : Int {
        return context.getSharedPreferences(PREF_NAME , Context.MODE_PRIVATE)
            .getInt("id" , -1)
    }

    fun deletePersonIdFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("id")
                .apply()
    }


    fun setShopId(context: Context , shopId: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt("shopId" , shopId)
            .apply()
    }

    fun getShopId(context: Context) : Int {
        return context.getSharedPreferences(PREF_NAME , Context.MODE_PRIVATE)
            .getInt("shopId" , -1)
    }

    fun deleteShopIdFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("shopId")
                .apply()
    }


    fun storeShop(context: Context , shop: Shop) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("shop" , Gson().toJson(shop))
                .apply()
    }

    fun fetchShop(context: Context): Shop? {
        val shopJson : String? = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("shop" , null)
        if (shopJson != null) {
            return Gson().fromJson(shopJson , Shop::class.java)
        } else {
            return null
        }
    }

    fun deleteShopFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("shop")
                .apply()
    }


    fun storeSeller(context: Context , seller: Seller) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("seller" , Gson().toJson(seller))
                .apply()
    }

    fun fetchSeller(context: Context): Seller? {
        val sellerJson : String? = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("seller" , null)
        if (sellerJson != null) {
            return Gson().fromJson(sellerJson , Seller::class.java)
        } else {
            return null
        }
    }

    fun deleteSellerFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("seller")
                .apply()
    }


    fun storeUser(context: Context , user: User) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("user" , Gson().toJson(user))
                .apply()
    }

    fun fetchUser(context: Context): User? {
        val userJson : String? = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("user" , null)
        if (userJson != null) {
            return Gson().fromJson(userJson , User::class.java)
        } else {
            return null
        }
    }

    fun deleteUserFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("user")
                .apply()
    }


    fun storeDiscount(context: Context, discount: Discount) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("discount" , Gson().toJson(discount))
                .apply()
    }

    fun fetchDiscount(context: Context): Discount? {
        val discountJson : String? = context
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getString("discount" , null)

        if (discountJson != null) {
            return Gson().fromJson(discountJson , Discount::class.java)
        } else {
            return null
        }
    }

    fun deleteDiscountFromCache(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("discount")
                .apply()
    }


    fun SetIntroSliderVisited(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("introSlider" , false)
            .apply()
    }

    fun showIntroSlider(context: Context) : Boolean{
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean("introSlider" , true)
    }
}