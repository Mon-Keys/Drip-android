package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Credential
import com.drip.dripapplication.domain.model.SignupResponse
import com.drip.dripapplication.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class AuthRepositoryImpl(
    private val api: DripApi
) : AuthRepository {
    override fun login(credential: Credential): Flow<ResultWrapper<Boolean>> = flow {
        emit(ResultWrapper.Loading)
        delay(2000)
        try {
            val response = api.login(credential.toRepModel())
            Timber.d("response = $response")
            if (response.status in 200..299) emit(ResultWrapper.Success(true))
            else emit(ResultWrapper.Error(Exception("Bad status response")))
        } catch (e: Exception) {
            Timber.e(e)
            emit(ResultWrapper.Error(e))
        }
    }
        .flowOn(Dispatchers.IO)

    override fun signup(credential: Credential): Flow<ResultWrapper<SignupResponse?>> = flow {
        emit(ResultWrapper.Loading)
        delay(1000)
        try {
            val response = api.signup(credential.toRepModel())
            Timber.i("$response")
            if (response.isSuccessful && response.body() != null) {
                Timber.i("UserDto=${response.body()}")
                val status = response.body()!!.status
                val userDto = response.body()!!.body
                val userDomain = userDto?.toDomainModel()
                emit(ResultWrapper.Success(userDomain))
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