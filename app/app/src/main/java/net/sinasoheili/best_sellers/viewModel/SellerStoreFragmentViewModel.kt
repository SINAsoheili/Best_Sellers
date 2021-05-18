package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.util.DataState

class SellerStoreFragmentViewModel
    constructor( private val shopRepository: ShopRepository) : ViewModel()
{
    val shopDataState: MutableLiveData<DataState<Shop>> = MutableLiveData()

    fun getShopInfo() {
        viewModelScope.launch {
            shopRepository.getShopInfo().onEach {
                shopDataState.value = it
            }.launchIn(viewModelScope)
        }
    }
}