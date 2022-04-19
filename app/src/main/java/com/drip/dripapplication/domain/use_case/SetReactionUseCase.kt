package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.domain.repository.LikeRepository

class SetReactionUseCase(private val repository: LikeRepository) {
    suspend fun execute(id: Long, reaction: Int) = repository.setReaction(id, reaction)
}