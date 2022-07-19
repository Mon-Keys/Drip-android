package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Credential
import com.drip.dripapplication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase (private val repository: AuthRepository) {
    operator fun invoke(credential: Credential): Flow<ResultWrapper<Boolean>> {
        return repository.login(credential)
    }

}