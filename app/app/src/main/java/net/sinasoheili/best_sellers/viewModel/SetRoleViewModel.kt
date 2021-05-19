package net.sinasoheili.best_sellers.viewModel

import android.content.Context
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
import net.sinasoheili.best_sellers.util.CacheToPreference
import net.sinasoheili.best_sellers.util.DataState

class SetRoleViewModel
constructor(
    private val context: Context,
    private val sellerRepository: SellerRepository,
    private val userRepository: UserRepository,
    private val shopRepository: ShopRepository
) : ViewModel()
{
    val sellerDataState: MutableLiveData<DataState<Boolean>> = MutableLiveData<DataState<Boolean>>()
    val userDataState: MutableLiveData<DataState<User>> = MutableLiveData<DataState<User>>()
    val shopDataState: MutableLiveData<DataState<Shop>> = MutableLiveData<DataState<Shop>>()

    fun registerSeller(seller: Seller, passwd: String) {
        viewModelScope.launch {

            sellerRepository.registerSeller(seller, passwd).onEach { it ->

                when (it) {
                    is DataState.Success<Seller> -> {
                        setStateForSeller(DataState.Success<Boolean>(true))
                    }

                    is DataState.Loading -> {
                        setStateForSeller(DataState.Loading())
                    }

                    is DataState.Error -> {
                        setStateForSeller(DataState.Error(it.text))
                    }

                    is DataState.ConnectionError -> {
                        setStateForSeller(DataState.ConnectionError(it.exception))
                    }
                }
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

                if( sellerState is DataState.Success<Boolean> ) { // if seller can login

                    //todo: remove read id from pref
                    shopRepository.checkUserHasShop(CacheToPreference.getPersonId(context)).onEach {
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

    private fun setStateForSeller(dataState: DataState<Boolean>) {
        sellerDataState.value = dataState
    }

    private fun setStateForShop(dataState: DataState<Shop>) {
        shopDataState.value = dataState
    }
}