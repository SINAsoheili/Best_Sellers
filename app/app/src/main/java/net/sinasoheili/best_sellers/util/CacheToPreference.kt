package net.sinasoheili.best_sellers.util

import android.content.Context
import com.google.gson.Gson
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop

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
}