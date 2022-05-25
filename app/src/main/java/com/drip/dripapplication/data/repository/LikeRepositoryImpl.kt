package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.LikeRequestBody
import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class LikeRepositoryImpl(
    private val api: DripApi
) : LikeRepository {
    override suspend fun setReaction(id:Long, reaction:Int): ResultWrapper<Boolean> {
        return try {
            val response = api.setReaction(LikeRequestBody(id,reaction))

            if (response.status == 200){
                val matchDto = response.body!!
                ResultWrapper.Success(200, matchDto.match)
            }else{
                ResultWrapper.Error(Exception("Bad response status"))
            }
        }catch (e: Exception){
            Timber.d(e)
            ResultWrapper.Error(e)
        }
    }
}
