package com.drip.dripapplication.domain.use_case

import com.drip.dripapplication.data.utils.ResultWrapper
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.model.UserRequest
import com.drip.dripapplication.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class EditProfileUseCase (private val repository: ProfileRepository) {
    operator fun invoke(user: UserRequest): Flow<ResultWrapper<User?>> {
        return repository.update(user)
    }
}