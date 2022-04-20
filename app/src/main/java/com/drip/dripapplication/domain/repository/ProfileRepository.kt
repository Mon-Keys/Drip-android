package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.model.SignupResponse
import com.drip.dripapplication.domain.model.UserRequest
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun update(user: UserRequest): Flow<ResultWrapper<User?>>
}