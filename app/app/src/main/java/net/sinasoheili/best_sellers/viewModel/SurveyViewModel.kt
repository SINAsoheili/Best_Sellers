package net.sinasoheili.best_sellers.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sinasoheili.best_sellers.model.AnsweredQuestion
import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.model.Question
import net.sinasoheili.best_sellers.repository.DiscountRepository
import net.sinasoheili.best_sellers.repository.MessageRepository
import net.sinasoheili.best_sellers.repository.QuestionRepository
import net.sinasoheili.best_sellers.util.DataState

class SurveyViewModel constructor(val messageRepository: MessageRepository,
                                  val questionRepository: QuestionRepository,
                                  val discountRepository: DiscountRepository)
    : ViewModel()
{
    val messageRegisterData: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val questionData: MutableLiveData<DataState<List<Question>>> = MutableLiveData()
    val submitQuestionData: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val userAnsQuestionData: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val userShopMessageData: MutableLiveData<DataState<Message>> = MutableLiveData()
    val answeredQuestionData: MutableLiveData<DataState<List<AnsweredQuestion>>> = MutableLiveData()
    val removeSurveyData: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val registerUserDiscountData: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    fun registerMessage(shopId: Int , message: String) {
            viewModelScope.launch {
                messageRepository.registerMessage(shopId , message).onEach {
                    messageRegisterData.value = it
                }.launchIn(viewModelScope)
            }
        }

    fun getQuestion(categoryId: Int) {
        viewModelScope.launch {
            questionRepository.getCategoryQuestion(categoryId).onEach {
                questionData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun submitQuestion(shopId: Int , response: Map<String, Int>) {
        viewModelScope.launch {
            questionRepository.submitQuestion(shopId , response).onEach {
                submitQuestionData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun checkUserAnsQuestion(shopId: Int) {
        viewModelScope.launch {
            questionRepository.checkUserAnsQuestion(shopId).onEach {
                userAnsQuestionData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getUserMessage(shopId: Int) {
        viewModelScope.launch {
            messageRepository.getUserShopMessage(shopId).onEach {
                userShopMessageData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getUserAnsweredQuestion(shopId: Int) {
        viewModelScope.launch {
            questionRepository.getUserAnsweredQuestion(shopId).onEach {
                answeredQuestionData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun removeSurvey(shopId: Int) {
        viewModelScope.launch {
            questionRepository.removeSurvey(shopId).onEach {
                removeSurveyData.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun registerNewUserDiscount(discountId: Int) {
        viewModelScope.launch{
            discountRepository.registerUserDiscount(discountId).onEach {
                registerUserDiscountData.value = it
            }.launchIn(viewModelScope)
        }
    }
}