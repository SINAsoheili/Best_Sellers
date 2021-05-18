package net.sinasoheili.best_sellers.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.repository.CategoryRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.util.DataState

class RegisterShopViewModel
constructor(
    private val context: Context,
    private val shopRepository: ShopRepository,
    private val categoryRepository: CategoryRepository
): ViewModel() {

    val shopDataState: MutableLiveData<DataState<Shop>> = MutableLiveData()
    val categoryDataState: MutableLiveData<DataState<List<ShopCategory>>> = MutableLiveData()

    fun registerShop(shop: Shop) {
        viewModelScope.launch {

            shopRepository.registerShop(shop).onEach { it ->
                setStateShop(it)
            }.launchIn(viewModelScope)

        }
    }

    fun getCategories() {
        viewModelScope.launch {

            categoryRepository.getShopCategories().onEach {
                categoryDataState.value = it
            }.launchIn(viewModelScope)

        }
    }

    private fun setStateShop(dataState: DataState<Shop>) {
        shopDataState.value = dataState
    }
}