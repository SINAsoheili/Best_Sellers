package net.sinasoheili.best_sellers.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.webService.*
import retrofit2.http.Query
import java.lang.Exception

class ShopRepository
constructor(
    val context: Context,
    val webService: WebService,
    val shopMapper: ShopMapper
) {

    suspend fun registerShop(shop: Shop) : Flow<DataState<Shop>> = flow {

        shop.idSeller = fetchSellerIdFromCache()

        emit(DataState.Loading())
        delay(1000)

        try {
            val registerShopEntity: RegisterShopEntity = webService.registerShop (
                name = shop.name,
                site = shop.site,
                description = shop.description,
                phone = shop.phone,
                address = shop.address,
                latitude = shop.latitude,
                longitude = shop.longitude,
                sellerId =  shop.idSeller,
                categoryId = shop.idCategory
            )

            if (registerShopEntity.statusRegister) {
                val shopFetched: Shop = shopMapper.toBase(registerShopEntity.shop)
                emit(DataState.Success<Shop>(shopFetched))
                cacheShopId(shopFetched.id)
                cacheShop(shopFetched)
            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun checkUserHasShop() : Flow<DataState<Shop>> = flow {
        val shop: Shop? = fetchShopFromCache()
        if (shop != null) {
            emit(DataState.Success<Shop>(shop))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {
                val sellerId: Int = fetchSellerIdFromCache()
                val shopInfoEntity: ShopInfoEntity = webService.checkUserHasShop(sellerId)

                if(shopInfoEntity.find) {
                    val shopFetched: Shop = shopMapper.toBase(shopInfoEntity.shop)
                    emit(DataState.Success<Shop>(shopFetched))
                    cacheShopId(shopFetched.id)
                    cacheShop(shopFetched)
                } else {
                    emit(DataState.Error(context.getString(R.string.user_does_not_have_shop)))
                }

            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
        }
    }

    suspend fun getShopInfo(shopId: Int = -1) : Flow<DataState<Shop>> = flow {

        val shop: Shop? = fetchShopFromCache()
        if(shop != null) {
            emit(DataState.Success(shop))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {
                val fetchedShopId: Int = if(shopId == -1) fetchShopIdFromCache() else shopId
                val shopInfoEntity: ShopInfoEntity = webService.getShopInfo(fetchedShopId)

                if(shopInfoEntity.find) {
                    val shopFetched: Shop = shopMapper.toBase(shopInfoEntity.shop)
                    emit(DataState.Success<Shop>(shopFetched))
                    cacheShop(shopFetched)
                } else {
                    emit(DataState.Error(context.getString(R.string.shop_not_found)))
                }
            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
        }
    }

    suspend fun deleteShop(): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val shopDeleteEntity: ShopDeleteEntity =  webService.deleteShop(fetchShopIdFromCache())
            if(shopDeleteEntity.shopDeleted) {
                deleteShopFromCache()
                emit(DataState.Success<Boolean>(true))
            } else {
                emit(DataState.Error(context.getString(R.string.delete_shop_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }

    }

    suspend fun searchShop(categoryId: Int , criteriaId: Int , shopName: String = "") : Flow<DataState<List<Shop>>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val shopListFetched: ShopListSearchEntity = if(shopName.isEmpty())
            {
                webService.shopSearch(categoryId , criteriaId)
            } else {
                webService.shopSearch(categoryId , criteriaId , shopName)
            }

            val result: MutableList<Shop> = mutableListOf()
            for (shopEntity in shopListFetched.shopList)
            {
                result.add(shopMapper.toBase(shopEntity))
            }
            emit(DataState.Success(result))

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    private fun cacheShopId(shopId: Int) {
        CacheToPreference.setShopId(context, shopId)
    }

    private fun fetchShopIdFromCache(): Int {
        return CacheToPreference.getShopId(context)
    }

    private fun cacheShop(shop: Shop) {
        CacheToPreference.storeShop(context, shop)
    }

    private fun fetchShopFromCache (): Shop? {
        return CacheToPreference.fetchShop(context)
    }

    private fun fetchSellerIdFromCache(): Int {
        val who: String? = CacheToPreference.getWhoLogIn(context)
        if (who == null){
            return -1
        } else {
            if (who.equals(Keys.SELLER)) {
                return CacheToPreference.getPersonId(context)
            } else {
                return -1
            }
        }
    }

    private fun deleteShopFromCache() {
        CacheToPreference.deleteWhoFromCache(context)
        CacheToPreference.deletePersonIdFromCache(context)
        CacheToPreference.deleteShopIdFromCache(context)
        CacheToPreference.deleteShopFromCache(context)
        CacheToPreference.deleteSellerFromCache(context)
        CacheToPreference.deleteDiscountFromCache(context)
    }
}