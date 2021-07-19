package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Criteria
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.repository.CategoryRepository
import net.sinasoheili.best_sellers.repository.MessageRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.util.DataState

class ShopSearchViewModel constructor(val categoryRepository: CategoryRepository ,
                                      val shopRepository: ShopRepository ,
                                      val messageRepository: MessageRepository): ViewModel() {

    val categoriesData: MutableLiveData<DataState<List<ShopCategory>>> = MutableLiveData()
    val criteriaData: MutableLiveData<DataState<List<Criteria>>> = MutableLiveData()
    val shopSearchData: MutableLiveData<DataState<List<Shop>>> = MutableLiveData()
    val commentData: MutableLiveData<DataState<List<Message>>> = MutableLiveData()

    fun getCategories() {
        viewModelScope.launch {
            categoryRepository.getShopCategories().onEach {
                categoriesData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getCriteria(categoryId: Int) {
        viewModelScope.launch {
            categoryRepository.getCriteria(categoryId).onEach {
                criteriaData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun searchShop(categoryId: Int , criteriaId: Int , city: String , shopName: String) {
        viewModelScope.launch {
            shopRepository.searchShop(categoryId , criteriaId , city, shopName ).onEach {
                shopSearchData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getShopComment(shopId: Int) {
        viewModelScope.launch {
            messageRepository.getShopMessages(shopId).onEach {
                commentData.value = it
            }.launchIn(viewModelScope)
        }

    }
}