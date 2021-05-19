package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.webService.RegisterShopEntity
import net.sinasoheili.best_sellers.webService.ShopInfoEntity
import net.sinasoheili.best_sellers.webService.ShopMapper
import net.sinasoheili.best_sellers.webService.WebService
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
                address = shop.address,
                latitude = 0F,
                longitude = 0F,
                sellerId =  shop.idSeller,
                categoryId = shop.idCategory
            )

            if (registerShopEntity.statusRegister) {
                val shop: Shop = shopMapper.toBase(registerShopEntity.shop)
                emit(DataState.Success<Shop>(shop))
                cacheShopId(shop.id)
                cacheShop(shop)
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

    suspend fun getShopInfo() : Flow<DataState<Shop>> = flow {

        val shop: Shop? = fetchShopFromCache()
        if(shop != null) {
            emit(DataState.Success(shop))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {

                val shopId: Int = fetchShopIdFromCache()
                if(shopId != -1)
                {
                    val shopInfoEntity: ShopInfoEntity = webService.getShopInfo(shopId)

                    if(shopInfoEntity.find) {
                        val shop: Shop = shopMapper.toBase(shopInfoEntity.shop)
                        emit(DataState.Success<Shop>(shop))
                        cacheShop(shop)
                    } else {
                        emit(DataState.Error(context.getString(R.string.shop_not_found)))
                    }
                }
            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
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
}