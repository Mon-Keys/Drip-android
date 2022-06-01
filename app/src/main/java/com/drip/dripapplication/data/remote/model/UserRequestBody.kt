package com.drip.dripapplication.data.remote.model

import com.drip.dripapplication.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRequestBody(
    val name: String,
    val date: String,
    val gender: String,
    val description: String?,
    val prefer: String?,
    @Json(name = "imgs")
    val images: List<String>,
    val tags: List<String>?
)
