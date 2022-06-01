package com.drip.dripapplication.presentation.profile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.R
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.use_case.GetUserInfoUseCase
import com.drip.dripapplication.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val useCase: GetUserInfoUseCase) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: LiveData<User?> = _userInfo

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = SingleLiveEvent<Int>()
    val errorMessage: SingleLiveEvent<Int>
        get() = _errorMessage

    fun getUserInfo(){
        viewModelScope.launch {
            useCase.invoke().collect {
                when (it){
                    is ResultWrapper.Loading -> {
                        _loadingState.value = true
                    }
                    is ResultWrapper.Error -> {
                        _errorMessage.value = R.string.error_from_network
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