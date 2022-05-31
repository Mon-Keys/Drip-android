package com.drip.dripapplication.domain.model

import com.drip.dripapplication.data.remote.model.UserDto

data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val description: String,
    val images: List<String>,
    val tags: List<String>
){
    fun toRepModel() = UserDto(id, name, age, description ?: "", images, tags ?: emptyList())

}
