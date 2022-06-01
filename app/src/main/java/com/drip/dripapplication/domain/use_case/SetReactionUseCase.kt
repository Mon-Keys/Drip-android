package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow

class SetReactionUseCase(private val repository: LikeRepository) {
    suspend fun execute(id: Long, reaction: Int): Flow<ResultWrapper<Boolean>> {
        return repository.setReaction(id, reaction)
    }
}