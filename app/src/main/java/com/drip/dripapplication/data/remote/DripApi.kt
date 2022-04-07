package com.drip.dripapplication.data.remote

import com.drip.dripapplication.data.remote.model.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface DripApi {
    @GET("api/v1/profile")
    suspend fun getUserInfo(): Response<UserDto>
}