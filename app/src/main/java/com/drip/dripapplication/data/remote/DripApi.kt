package com.drip.dripapplication.data.remote

import com.drip.dripapplication.data.remote.model.LikeRequestBody
import com.drip.dripapplication.data.remote.model.ResponseWrapper
import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.remote.model.CridentialDto
import com.drip.dripapplication.data.remote.model.SignupResponseDto
import com.drip.dripapplication.data.remote.model.UsersWrapper
import com.drip.dripapplication.domain.model.User
import retrofit2.Response
import retrofit2.http.*

interface DripApi {
    @GET("api/v1/auth/profile")
    suspend fun getUserInfo(): Response<ResponseWrapper<UserDto>>

    @POST("api/v1/auth/session")
    suspend fun login(@Body cridential: CridentialDto): Response<ResponseWrapper<UserDto>>

    @POST("api/v1/auth/profile")
    suspend fun signup(@Body cridential: CridentialDto): Response<ResponseWrapper<SignupResponseDto>>

    @GET("api/v1/user/cards")
    suspend fun getUsers(): ResponseWrapper<UsersWrapper<List<UserDto>?>>

    @POST("api/v1/likes")
    suspend fun setReaction(@Body request: LikeRequestBody)
}