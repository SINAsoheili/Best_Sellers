package net.sinasoheili.best_sellers.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.*
import java.lang.Exception

class DiscountRepository constructor(val context: Context,
                                     val webService: WebService,
                                     val discountMapper: DiscountMapper)
{

    suspend fun registerDiscount(discount: Discount): Flow<DataState<Discount>> = flow<DataState<Discount>> {
        emit(DataState.Loading())
        delay(1000)

        discount.idShop = getShopIdFromCache()

        try{
            val registerDiscountEntity: RegisterDiscountEntity = webService.discountRegister(discount.title , discount.amount , discount.idShop)

            if (registerDiscountEntity.statusRegister){

                val discountEntity: DiscountEntity = registerDiscountEntity.discount
                val fetchedDiscount: Discount = discountMapper.toBase(discountEntity)
                emit(DataState.Success(fetchedDiscount))
                storeDiscountToCache(fetchedDiscount)

            } else { // can't create new discount.
                emit(DataState.Error(context.getString(R.string.can_not_create_new_discount)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun deleteDiscount() : Flow<DataState<Boolean>> = flow {

        emit(DataState.Loading())
        delay(10000)

        try {
            val discountDeleteEntity: DiscountDeleteEntity = webService.deleteDiscount(getShopIdFromCache())

            if(discountDeleteEntity.deleteStatus) {
                deleteDiscountFromCache()
                emit(DataState.Success(true))
            } else {
                emit(DataState.Error(context.getString(R.string.there_is_not_any_discount_to_delete)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun getShopDiscount() : Flow<DataState<Discount>> = flow {
        val cacheDiscount: Discount? = fetchDiscountFromCache()
        if (cacheDiscount != null){
            emit(DataState.Success(cacheDiscount))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {
                val discountResponse: GetDiscountResponse = webService.getShopDiscount(getShopIdFromCache())
                if(discountResponse.find) {
                    val discount: Discount = discountMapper.toBase(discountResponse.discount)
                    emit(DataState.Success(discount))
                    storeDiscountToCache(discount)
                } else {
                    emit(DataState.Error(context.getString(R.string.there_is_not_any_Discount_for_this_shop)))
                }

            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
        }
    }

    suspend fun checkUserHasDiscount(userId: Int): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val checkResponse: CheckUserHasDiscountResponse = webService.checkUserHasDiscount(getShopIdFromCache() , userId)
            emit(DataState.Success(checkResponse.discountAvailable))

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun deleteUserDiscount(userId: Int): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val deleteUserDiscountResponse: DeleteUserDiscountResponse = webService.deleteDiscountOfUser(getShopIdFromCache(), userId)
            if (deleteUserDiscountResponse.deleteStatus) {
                emit(DataState.Success(true))
            } else {
                emit(DataState.Error(context.getString(R.string.delete_discount_of_user_was_not_successful)))
            }
        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    private fun getShopIdFromCache(): Int {
        return CacheToPreference.getShopId(context)
    }

    private fun storeDiscountToCache(discount: Discount) {
        CacheToPreference.storeDiscount(context, discount)
    }

    private fun fetchDiscountFromCache(): Discount? {
        return CacheToPreference.fetchDiscount(context)
    }

    private fun deleteDiscountFromCache() {
        CacheToPreference.deleteDiscountFromCache(context)
    }
}