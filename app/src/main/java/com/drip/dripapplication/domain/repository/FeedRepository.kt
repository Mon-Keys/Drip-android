package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getFeed(): Flow<ResultWrapper<List<User>?>>
}