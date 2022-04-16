package com.drip.dripapplication.data.remote

import com.drip.dripapplication.data.remote.model.ResponseWrapper
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.remote.model.CridentialDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface DripApi {
    @GET("api/v1/auth/profile")
    suspend fun getUserInfo(): Response<ResponseWrapper<UserDto>>

    @POST("api/v1/auth/session")
    suspend fun login(user: CridentialDto): Response<ResponseWrapper<UserDto>>
}