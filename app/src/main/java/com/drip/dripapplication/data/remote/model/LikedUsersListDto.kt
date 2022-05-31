package com.drip.dripapplication.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikedUsersListDto(
    @Json(name = "allUsers")
    val users: List<UserDto>?
)
