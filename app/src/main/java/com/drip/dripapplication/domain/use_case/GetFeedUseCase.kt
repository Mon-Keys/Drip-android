package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetFeedUseCase(private val repository: UserRepository) {
    fun execute(): Flow<ResultWrapper<List<User>?>>{
        return repository.getFeed()
    }
}