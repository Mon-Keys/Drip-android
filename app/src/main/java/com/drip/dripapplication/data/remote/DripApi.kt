package com.drip.dripapplication.data.remote

import com.drip.dripapplication.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface DripApi {
    // Todo (Убрать внешний Response)
    @GET("api/v1/auth/profile")
    suspend fun getUserInfo(): Response<ResponseWrapper<UserDto>>

    @POST("api/v1/auth/session")
    suspend fun login(@Body cridential: CridentialDto): Response<ResponseWrapper<UserDto>>

    @POST("api/v1/auth/profile")
    suspend fun signup(@Body cridential: CridentialDto): Response<ResponseWrapper<SignupResponseDto>>

    @PUT("api/v1/profile")
    suspend fun update(@Body user: UserRequestBody): Response<ResponseWrapper<UserDto>>

    @GET("api/v1/user/cards")
    suspend fun getUsers(): ResponseWrapper<UsersListDto>

    @GET("api/v1/user/likes")
    suspend fun getLikes(): ResponseWrapper<LikedUsersListDto>

    @POST("api/v1/likes")
    suspend fun setReaction(@Body request: LikeRequestBody): ResponseWrapper<MatchDto>
}