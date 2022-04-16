package com.drip.dripapplication.domain.repository

interface LikeRepository{
    suspend fun setReaction(id:Long, reaction: Int)
}
