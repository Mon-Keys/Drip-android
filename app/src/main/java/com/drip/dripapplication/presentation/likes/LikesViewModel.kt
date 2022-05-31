package com.drip.dripapplication.presentation.likes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetLikesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LikesViewModel(private val useCase: GetLikesUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<List<User>?>()
    val userInfo: LiveData<List<User>?> = _userInfo

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _status = MutableLiveData<Int>()
    val status: LiveData<Int> = _status

    fun getLikes() {
        viewModelScope.launch {
            useCase.invoke().collect {
                when (it) {
                    is ResultWrapper.Loading -> {
                        println("Идет загрузка")
                        _loadingState.value = true
                    }
                    is ResultWrapper.Error -> {
                        println(it.exception)
                        _loadingState.value = false
                    }
                    is ResultWrapper.Success -> {
                        _loadingState.value = false
                        _userInfo.value = it.data
                        _status.value = it.status
                    }
                }
            }
        }
    }
}