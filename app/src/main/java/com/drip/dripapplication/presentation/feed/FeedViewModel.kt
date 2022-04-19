package com.drip.dripapplication.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetFeedUseCase
import com.drip.dripapplication.domain.use_case.SetReactionUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedViewModel(
    private val useCaseFeed: GetFeedUseCase,
    private val useCaseReaction: SetReactionUseCase
) : ViewModel() {
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> = _userInfo

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> = _errorMessage


    private var currentIndex = 0

    private val users = mutableListOf<User>()

    init {
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            useCaseFeed.execute().collect {
                when (it){
                    is ResultWrapper.Loading -> {
                        Timber.d("Идет загрузка")
                    }
                    is ResultWrapper.Error -> {
                        _errorMessage.value = R.string.error_from_repository
                    }
                    is ResultWrapper.Success -> {
                        Timber.d("Дошли сюда")
                        if (it.data == null){
                            TODO("Дошли до конца списка, нужно выполнить снова запрос к api," +
                                    "если не получится, то сделать перерыв")
                        }else{
                            users.addAll(it.data)
                            getOneUser()
                        }
                    }
                }
            }
        }
    }

    private fun getOneUser(){
        Timber.d("lastindex = ${users.lastIndex}, currentIndex = $currentIndex")
        if (users.isEmpty()){
            getUsers()
        }else if (users.isNotEmpty() && currentIndex == users.lastIndex){
            getUsers()
            currentIndex += 1
        }else{
            _userInfo.value = users[currentIndex]
        }
    }

    fun swipe(userId: Long, reaction:Int){
        currentIndex += 1
        viewModelScope.launch {
            useCaseReaction.execute(userId, reaction)
        }
        getOneUser()

    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared")
    }
}