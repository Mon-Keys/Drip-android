package com.drip.dripapplication.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.use_case.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel (private val useCase: LoginUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun login(cridential: Cridential){
        viewModelScope.launch {
            useCase.invoke(cridential).collect {
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