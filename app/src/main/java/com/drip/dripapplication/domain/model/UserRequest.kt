package com.drip.dripapplication.domain.model

import com.drip.dripapplication.data.remote.model.UserDto
import com.drip.dripapplication.data.remote.model.UserRequestBody

data class UserRequest(
    val name: String,
    val date: String,
    val gender: String,
    val description: String,
    val prefer: String?,
    val images: List<String>,
    val tags: List<String>
) {
    fun toRepModel() =
        UserRequestBody(
            name,
            date,
            gender,
            description ?: "",
            prefer ?: "",
            images,
            tags ?: emptyList()
        )
}
