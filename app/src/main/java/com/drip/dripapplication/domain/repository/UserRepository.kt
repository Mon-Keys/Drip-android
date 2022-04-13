package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<ResultWrapper<User?>>

    fun getFeed(): Flow<ResultWrapper<List<User>>>
}
