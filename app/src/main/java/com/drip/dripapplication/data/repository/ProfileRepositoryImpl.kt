package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.UserRequest
import com.drip.dripapplication.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class ProfileRepositoryImpl(
    private val api: DripApi
) : ProfileRepository {
    override fun update(user: UserRequest): Flow<ResultWrapper<User?>> = flow {
        emit(ResultWrapper.Loading)
        delay(1000)
        try {
            val response = api.update(user.toRepModel())
            Timber.i("$response")
            if (response.isSuccessful && response.body() != null) {
                Timber.i("UserDto=${response.body()}")
                val status = response.body()!!.status
                val userDto = response.body()!!.body
                val userDomain = userDto?.toDomainModel()
                emit(ResultWrapper.Success(status, userDomain))
            } else {
                Timber.i("status=${response.isSuccessful}")
                TODO("Берем данные из кэша")
            }
        } catch (e: Exception) {
            Timber.e(e)
            TODO("Берем данные из кэша")
        }
    }
        .flowOn(Dispatchers.IO)
}