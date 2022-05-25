package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface LikeRepository{
    suspend fun setReaction(id:Long, reaction: Int): ResultWrapper<Boolean>
}
