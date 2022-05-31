package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.Cridential
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetLikesUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<ResultWrapper<List<User>?>> {
        return repository.getLikes()
    }
}