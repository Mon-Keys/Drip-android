package com.drip.dripapplication.domain.repository

import com.drip.dripapplication.domain.model.User

interface UserRepository {
    fun getUser(): User
}
