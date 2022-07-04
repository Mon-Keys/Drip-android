package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.domain.repository.PreferencesRepository

class CheckSignInUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(): String? = repository.getToken()

}