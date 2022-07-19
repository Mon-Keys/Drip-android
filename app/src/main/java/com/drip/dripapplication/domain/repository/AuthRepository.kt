package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Credential
import com.drip.dripapplication.domain.model.SignupResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(credential: Credential): Flow<ResultWrapper<Boolean>>
    fun signup(credential: Credential): Flow<ResultWrapper<SignupResponse?>>
}