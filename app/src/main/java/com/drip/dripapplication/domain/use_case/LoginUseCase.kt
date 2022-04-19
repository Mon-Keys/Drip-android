package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase (private val repository: AuthRepository) {
    operator fun invoke(cridential: Cridential): Flow<ResultWrapper<User?>> {
        return repository.login(cridential)
    }
}