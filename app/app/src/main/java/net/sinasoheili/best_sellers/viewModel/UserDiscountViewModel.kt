package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.ShopDiscount
import net.sinasoheili.best_sellers.repository.DiscountRepository
import net.sinasoheili.best_sellers.util.DataState

class UserDiscountViewModel constructor(val discountRepository: DiscountRepository) : ViewModel(){

    val discountListData: MutableLiveData<DataState<ArrayList<ShopDiscount>>> = MutableLiveData()

    fun getUserDiscount() {
        viewModelScope.launch {
            discountRepository.getUserDiscountList().onEach {
                discountListData.value = it
            }.launchIn(viewModelScope)
        }
    }
}