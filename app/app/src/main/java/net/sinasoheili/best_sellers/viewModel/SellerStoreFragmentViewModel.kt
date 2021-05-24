package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.util.DataState

class SellerStoreFragmentViewModel
    constructor( private val shopRepository: ShopRepository,
                 private val sellerRepository: SellerRepository) : ViewModel()
{
    val shopDataState: MutableLiveData<DataState<Shop>> = MutableLiveData()
    val sellerDataState: MutableLiveData<DataState<Seller>> = MutableLiveData()
    val deleteShopDataState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val deleteSellerDataState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    fun getShopInfo() {
        viewModelScope.launch {
            shopRepository.getShopInfo().onEach {
                shopDataState.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getSellerInfo() {
        viewModelScope.launch {
            sellerRepository.getSellerInfo().onEach {
                sellerDataState.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun deleteShop() {
        viewModelScope.launch {
            shopRepository.deleteShop().onEach {
                deleteShopDataState.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun deleteSeller() {
        viewModelScope.launch {
            sellerRepository.deleteSeller().onEach {
                deleteSellerDataState.value = it
            }.launchIn(viewModelScope)
        }
    }
}