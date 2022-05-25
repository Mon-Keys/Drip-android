package com.drip.dripapplication.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsersListDto(
    @Json(name = "Users")
    val users: List<UserDto>?
)
