package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.DataState
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

                emit(DataState.Success<Seller>(sellerMapper.toBase(sellerEntity.seller)))

            } else {
                emit(DataState.Error(context.getString(R.string.insert_was_not_successful)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun loginSeller(phone: String, passwd: String) : Flow<DataState<Seller>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {

            val sellerLoginEntity: LoginSellerEntity =  webService.loginSeller(
                    phone = phone,
                    passwd = passwd
            )

            if (sellerLoginEntity.loginStatus) { //get seller

                try {
                    val seller: Seller? = getSellerInfo(sellerLoginEntity.sellerId)
                    emit(DataState.Success<Seller>(seller!!))
                } catch (e: Exception) {
                    emit(DataState.ConnectionError(e))
                }

            } else {
                emit(DataState.Error(context.getString(R.string.user_was_not_found)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    @Throws(Exception::class) //for connection error
    suspend fun getSellerInfo(sellerId: Int): Seller? {

        val sellerInfoEntity : SellerInfoEntity = webService.getSellerInfo(sellerId = sellerId)

        if(sellerInfoEntity.findStatus) { //seller found
            return sellerMapper.toBase(sellerInfoEntity.seller)
        } else { //seller not found with this id
            return null
        }
    }
}