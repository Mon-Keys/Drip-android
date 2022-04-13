package com.drip.dripapplication.data.remote

import com.drip.dripapplication.data.remote.model.ResponseWrapper
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.domain.model.User
import retrofit2.Response
import retrofit2.http.GET

interface DripApi {
    @GET("api/v1/auth/profile")
    suspend fun getUserInfo(): Response<ResponseWrapper<UserDto>>

    @GET("api/v1/feed")
    suspend fun getUsers(): ResponseWrapper<List<UserDto>>
}