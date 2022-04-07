package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val api: DripApi
    // private val dao: UserDao
) : UserRepository {
    override fun getUser(): Flow<ResultWrapper<User?>> = flow{
        emit(ResultWrapper.Loading)
        delay(5000)
        try {
            val response = api.getUserInfo()
            if (response.isSuccessful && response.body()!=null){
                val userDto = response.body()
                val userDomain = userDto?.toDomainModel()
                emit(ResultWrapper.Success(userDomain))
            }else
                TODO("Берем данные из кэша")
        }catch (e: Exception){
            emit(ResultWrapper.Error(e))
        }
    }
        .flowOn(Dispatchers.IO)
}
