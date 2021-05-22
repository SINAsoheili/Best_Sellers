package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.repository.StatisticRepository
import net.sinasoheili.best_sellers.util.DataState

class SellerStatisticViewModel constructor(private val statisticRepository: StatisticRepository) : ViewModel() {

    val statisticData: MutableLiveData<DataState<ArrayList<Statistic>>> = MutableLiveData()

    fun getStatistic() {
        viewModelScope.launch {
            statisticRepository.getStatistics().onEach {
                statisticData.value = it
            }.launchIn(viewModelScope)
        }
    }

}