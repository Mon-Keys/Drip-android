package com.drip.dripapplication.data.repository

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.domain.model.User
import com.drip.dripapplication.domain.repository.UserRepository

class UserRepositoryImpl(
    private val api: DripApi
    // private val dao: UserDao
) : UserRepository {
    override fun getUser(): User {
        TODO("Not yet implemented")
    }
}
