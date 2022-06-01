package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.FeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import kotlin.random.Random

class FeedRepositoryImpl(private val api: DripApi): FeedRepository {
    override fun getFeed(): Flow<ResultWrapper<List<User>?>> = flow{
        emit(ResultWrapper.Loading)
        delay(500)
        try {
            val response = api.getUsers()
            Timber.d("responseBody = ${response.body}")
            if (response.status == 200 && response.body != null){
                val usersDto = response.body.users
                val userDomain = usersDto?.map{
                    it.toDomainModel()
                }
                emit(ResultWrapper.Success(200,userDomain))

            }else{
                emit(ResultWrapper.Error(Exception("Some bad response code or body")))
            }
        }catch (e: Exception){
            emit(ResultWrapper.Error(e))
        }
    }.flowOn(Dispatchers.IO)

}