package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.webService.*
import java.lang.Exception

class SellerRepository constructor(
    private val context: Context,
    private val webService: WebService,
    private val sellerMapper: SellerMapper
    )
{
    suspend fun registerSeller(seller: Seller , passwd: String) : Flow<DataState<Seller>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val sellerEntity: RegisterSellerEntity = webService.registerSeller(
                seller.name,
                seller.lastName,
                seller.phone,
                passwd
            )

            if (sellerEntity.isRegister) {
                val sellerFetched: Seller = sellerMapper.toBase(sellerEntity.seller)
                emit(DataState.Success<Seller>(sellerFetched))
                cacheSellerId(sellerFetched.id)
                cacheSellerToPreference(sellerFetched)
            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun loginSeller(phone: String, passwd: String) : Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {

            val sellerLoginEntity: LoginSellerEntity =  webService.loginSeller(
                    phone = phone,
                    passwd = passwd
            )

            if (sellerLoginEntity.loginStatus) {

                cacheSellerId(sellerLoginEntity.sellerId)
                emit(DataState.Success<Boolean>(true))

            } else {
                emit(DataState.Error(context.getString(R.string.user_not_found)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun getSellerInfo(): Flow<DataState<Seller>> = flow {
        val seller: Seller? = fetchSellerFromPreference()
        if (seller != null) {
            emit(DataState.Success<Seller>(seller))
        } else {
            emit(DataState.Loading())
            delay(1000)

            try {
                val sellerId: Int = fetchSellerIdFromCache()
                val sellerInfoEntity: SellerInfoEntity = webService.getSellerInfo(sellerId)

                if(sellerInfoEntity.findStatus) { //if seller found
                    val sellerObj: Seller = SellerMapper().toBase(sellerInfoEntity.seller)
                    emit(DataState.Success<Seller>(sellerObj))
                    cacheSellerToPreference(sellerObj)
                } else { // if seller not found
                    emit(DataState.Error(context.getString(R.string.seller_not_found)))
                }

            } catch (e: Exception) {
                emit(DataState.ConnectionError(e))
            }
        }
    }

    private fun cacheSellerId(id: Int) {
        CacheToPreference.setWhoLogIn(context , Keys.SELLER)
        CacheToPreference.setIdPerson(context , id)
    }

    private fun fetchSellerIdFromCache(): Int {
        val who: String? = CacheToPreference.getWhoLogIn(context)
        if (who == null) {
            return -1
        } else {
            if (who.equals(Keys.SELLER)) {
                return CacheToPreference.getPersonId(context)
            } else {
                return -1
            }
        }
    }

    private fun cacheSellerToPreference(seller: Seller) {
        CacheToPreference.storeSeller(context, seller)
    }

    private fun fetchSellerFromPreference() : Seller?{
        return CacheToPreference.fetchSeller(context)
    }
}