package net.sinasoheili.best_sellers.repository

import android.content.Context
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
                emit(DataState.Success(true))
                deleteDiscountFromCache()
            } else {
                emit(DataState.Error(context.getString(R.string.there_is_not_any_discount_to_delete)))
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