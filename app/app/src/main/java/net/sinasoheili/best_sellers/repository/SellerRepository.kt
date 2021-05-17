package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.RegisterSellerEntity
import net.sinasoheili.best_sellers.webService.SellerMapper
import net.sinasoheili.best_sellers.webService.WebService
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
}