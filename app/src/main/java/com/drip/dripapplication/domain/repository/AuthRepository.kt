package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.model.SignupResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(cridential: Cridential): Flow<ResultWrapper<User?>>
    fun signup(cridential: Cridential): Flow<ResultWrapper<SignupResponse?>>
}