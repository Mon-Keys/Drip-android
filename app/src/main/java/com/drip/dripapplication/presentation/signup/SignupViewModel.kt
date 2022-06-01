package com.drip.dripapplication.presentation.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.model.SignupResponse
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val useCase: SignupUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<SignupResponse?>()
    val userInfo: LiveData<SignupResponse?> = _userInfo

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _status = MutableLiveData<Int>()
    val status: LiveData<Int> = _status

    fun signup(cridential: Cridential) {
        viewModelScope.launch {
            useCase.invoke(cridential).collect {
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