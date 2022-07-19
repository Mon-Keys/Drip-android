package com.drip.dripapplication.presentation.main

import androidx.lifecycle.ViewModel
import com.drip.dripapplication.domain.repository.PreferencesRepository
import com.drip.dripapplication.domain.use_case.CheckSignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: CheckSignInUseCase
    ): ViewModel()
{
    fun checkLogin(): Boolean = !useCase.invoke().isNullOrEmpty()
}