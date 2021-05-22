package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.repository.MessageRepository
import net.sinasoheili.best_sellers.repository.StatisticRepository
import net.sinasoheili.best_sellers.util.DataState

class SellerStatisticViewModel constructor(private val statisticRepository: StatisticRepository ,
                                           private val messageRepository: MessageRepository) : ViewModel() {

    val statisticData: MutableLiveData<DataState<ArrayList<Statistic>>> = MutableLiveData()
    val messageData: MutableLiveData<DataState<List<Message>>> = MutableLiveData()

    fun getStatistic() {
        viewModelScope.launch {
            statisticRepository.getStatistics().onEach {
                statisticData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getMessages() {
        viewModelScope.launch {
            messageRepository.getShopMessages().onEach {
                messageData.value = it
            }.launchIn(viewModelScope)
        }
    }
}