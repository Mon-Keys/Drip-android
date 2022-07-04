package com.drip.dripapplication.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drip.dripapplication.R
import kotlinx.coroutines.flow.collect
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Credential
import com.drip.dripapplication.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: LoginUseCase) : ViewModel() {
    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private val _loginButton = MutableLiveData<Boolean>()
    val loginButton: LiveData<Boolean> = _loginButton

    private val _validEmail = MutableLiveData<Validation>()
    val validEmail: LiveData<Validation> = _validEmail

    private val _validPassword = MutableLiveData<Validation>()
    val validPassword: LiveData<Validation> = _validPassword

    init {
        updateButtonStatus()
    }

    fun validateEmail(text: CharSequence?){
        when {
            text.isNullOrEmpty() -> _validEmail.value = Validation(false, null)
            Patterns.EMAIL_ADDRESS.matcher(text).matches() -> _validEmail.value = Validation(true, null)
            else -> _validEmail.value = Validation(false, R.string.error_email)
        }
        updateButtonStatus()
    }

    fun validatePassword(text: CharSequence?){
        when {
            text.isNullOrEmpty() -> _validPassword.value = Validation(false, null)
            text.length < 8 -> _validPassword.value = Validation(false, R.string.error_short_password)
            text.length > 20 -> _validPassword.value = Validation(false, R.string.error_long_password)
            else -> _validPassword.value = Validation(true, null)
        }
        updateButtonStatus()
    }

    private fun updateButtonStatus(){
        _loginButton.value = _validPassword.value?.isValid == true && _validEmail.value?.isValid == true
    }


    fun login(credential: Credential) {
        viewModelScope.launch {
            useCase.invoke(credential).collect {
                when (it) {
                    is ResultWrapper.Loading -> {
                        Timber.d("Идет загрузка")
                        _loginButton.value = false
                    }
                    is ResultWrapper.Error -> {
                        Timber.d(it.exception)
                        _loginButton.value = true
                        _isLogin.value = false
                    }
                    is ResultWrapper.Success -> {
                        Timber.d("Успешно!")
                        _isLogin.value = it.data
                    }
                }
            }
        }
    }

}
