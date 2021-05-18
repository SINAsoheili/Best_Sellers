package net.sinasoheili.best_sellers.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.ManageLogin
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

        shop.idSeller = ManageLogin.getPersonId(context)

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
            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun checkUserHasShop(sellerId: Int) : Flow<DataState<Shop>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val shopInfoEntity: ShopInfoEntity = webService.checkUserHasShop(sellerId)

            if(shopInfoEntity.find) {
                val shop: Shop = shopMapper.toBase(shopInfoEntity.shop)
                emit(DataState.Success<Shop>(shop))
                cacheShopId(shop.id)
            } else {
                emit(DataState.Error(context.getString(R.string.user_does_not_have_shop)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    private fun cacheShopId(shopId: Int) {
        ManageLogin.setShopId(context, shopId)
    }
}