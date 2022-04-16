package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.remote.model.LikeRequestBody
import com.drip.dripapplication.domain.repository.LikeRepository
import timber.log.Timber

class LikeRepositoryImpl(
    private val api: DripApi
) : LikeRepository {
    override suspend fun setReaction(id:Long, reaction:Int) {
        try {
            api.setReaction(LikeRequestBody(id,reaction))
        }catch (e: Exception){
            Timber.d(e)
        }
    }
}
