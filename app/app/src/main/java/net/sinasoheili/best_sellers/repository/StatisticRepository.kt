package net.sinasoheili.best_sellers.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.StatisticEntity
import net.sinasoheili.best_sellers.webService.StatisticMapper
import net.sinasoheili.best_sellers.webService.StatisticResponse
import net.sinasoheili.best_sellers.webService.WebService

class StatisticRepository constructor( val context: Context,
                                       val webService: WebService,
                                       val statisticMapper: StatisticMapper)
{

    suspend fun getStatistics () : Flow<DataState<ArrayList<Statistic>>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val shopId: Int = getShopIdFromCache()
            val statisticResponse: StatisticResponse = webService.getStatistic(shopId)

            val arrStatistics: Array<StatisticEntity> = statisticResponse.statistics
            val statisticResult: ArrayList<Statistic> = arrayListOf()
            for (i in arrStatistics) {
                statisticResult.add(statisticMapper.toBase(i))
            }
            emit(DataState.Success(statisticResult))
        } catch(e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }

    private fun getShopIdFromCache(): Int {
        return CacheToPreference.getShopId(context)
    }
}