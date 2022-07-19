package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.domain.repository.PreferencesRepository

class LogoutUseCase(private val repository: PreferencesRepository) {
    operator fun invoke(): Boolean{
        return repository.deleteAll()
    }
}