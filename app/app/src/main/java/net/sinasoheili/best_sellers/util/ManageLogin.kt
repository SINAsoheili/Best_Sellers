package net.sinasoheili.best_sellers.util

import android.content.Context

object ManageLogin {

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
}