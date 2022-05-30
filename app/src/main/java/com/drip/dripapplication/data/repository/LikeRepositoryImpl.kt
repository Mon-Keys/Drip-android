package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.LikeRequestBody
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.repository.LikeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class LikeRepositoryImpl(
    private val api: DripApi
) : LikeRepository {
    override suspend fun setReaction(id:Long, reaction:Int): Flow<ResultWrapper<Boolean>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val response = api.setReaction(LikeRequestBody(id,reaction))
            if (response.status == 200){
                val matchDto = response.body!!
                emit(ResultWrapper.Success(200, matchDto.match))
            }else{
                emit(ResultWrapper.Success(200, true))
                //emit(ResultWrapper.Error(Exception("Bad response status")))
            }
        }catch (e: Exception){
            Timber.d(e)
            emit(ResultWrapper.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
