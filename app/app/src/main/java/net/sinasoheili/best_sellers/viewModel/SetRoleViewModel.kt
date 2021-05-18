package net.sinasoheili.best_sellers.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.repository.UserRepository
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.util.Keys
import net.sinasoheili.best_sellers.util.ManageLogin

class SetRoleViewModel
constructor(
    private val context: Context,
    private val sellerRepository: SellerRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository
) : ViewModel()
{
    val sellerDataState: MutableLiveData<DataState<Seller>> = MutableLiveData<DataState<Seller>>()
    val userDataState: MutableLiveData<DataState<User>> = MutableLiveData<DataState<User>>()
    val shopDataState: MutableLiveData<DataState<Shop>> = MutableLiveData<DataState<Shop>>()

    fun registerSeller(seller: Seller, passwd: String) {
        viewModelScope.launch {

            sellerRepository.registerSeller(seller, passwd).onEach { it ->
                setStateForSeller(it)
            }.launchIn(viewModelScope)
        }
    }

    fun registerUser(user: User, passwd: String) {
        viewModelScope.launch {

            userRepository.registerUser(user, passwd).onEach { it ->
                setStateForUser(it)
            }.launchIn(viewModelScope)

        }
    }

    fun logInUser(phone: String, passwd: String) {
        viewModelScope.launch {

            userRepository.loginUser(phone, passwd).onEach { it ->
                setStateForUser(it)
            }.launchIn(viewModelScope)
        }
    }

    fun logInSeller(phone: String, passwd: String) {
        viewModelScope.launch {

            sellerRepository.loginSeller(phone= phone, passwd= passwd).onEach { sellerState ->

                if( sellerState is DataState.Success<Seller> ) { // if seller can login

                    shopRepository.checkUserHasShop(sellerState.data.id).onEach {
                        setStateForShop(it)
                    }.launchIn(viewModelScope)

                } else {
                    setStateForSeller(sellerState)
                }

            }.launchIn(viewModelScope)

        }
    }

    private fun setStateForUser(dataState: DataState<User>) {
        userDataState.value = dataState
    }

    private fun setStateForSeller(dataState: DataState<Seller>) {
        sellerDataState.value = dataState
    }

    private fun setStateForShop(dataState: DataState<Shop>) {
        shopDataState.value = dataState
    }
}