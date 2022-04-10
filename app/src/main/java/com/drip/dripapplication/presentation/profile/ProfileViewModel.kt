package com.drip.dripapplication.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val useCase: GetUserInfoUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        viewModelScope.launch {
            useCase.invoke().collect {
                when (it){
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
                    }
                }
            }
        }
    }

}