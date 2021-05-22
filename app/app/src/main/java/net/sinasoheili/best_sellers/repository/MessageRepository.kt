package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.MessageEntity
import net.sinasoheili.best_sellers.webService.MessageMapper
import net.sinasoheili.best_sellers.webService.ShopGetMessageResponse
import net.sinasoheili.best_sellers.webService.WebService
import java.lang.Exception

class MessageRepository constructor(val context: Context,
                                    private val webService: WebService,
                                    private val messageMapper: MessageMapper)
{
    suspend fun getShopMessages() : Flow<DataState<MutableList<Message>>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val  shopResponse: ShopGetMessageResponse = webService.shopGetMessage(getShopIdFromCache())
            if (shopResponse.find) {
                val messageListEntity: List<MessageEntity> = shopResponse.messages
                val messageResult: MutableList<Message> = arrayListOf()
                for (i in messageListEntity) {
                    messageResult.add(messageMapper.toBase(i))
                }
                emit(DataState.Success(messageResult))
            } else {
                emit(DataState.Error(context.getString(R.string.message_not_found)))
            }

        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    private fun getShopIdFromCache() : Int{
        return CacheToPreference.getShopId(context)
    }
}