package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.UserRepository
import net.sinasoheili.best_sellers.util.DataState

class SetRoleViewModel
constructor(
    private val sellerRepository: SellerRepository,
    private val userRepository: UserRepository
) : ViewModel()
{
    val sellerDataState: MutableLiveData<DataState<Seller>> = MutableLiveData<DataState<Seller>>()
    val userDataState: MutableLiveData<DataState<User>> = MutableLiveData<DataState<User>>()

    fun registerSeller(seller: Seller, passwd: String) {
        viewModelScope.launch {

            sellerRepository.registerSeller(seller, passwd).onEach { it ->
                sellerDataState.value = it
            }.launchIn(viewModelScope)

        }

    }

    fun registerUser(user: User, passwd: String) {
        viewModelScope.launch {

            userRepository.registerUser(user, passwd).onEach { it ->
                    userDataState.value = it
            }.launchIn(viewModelScope)

        }
    }

}