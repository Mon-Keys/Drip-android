package com.drip.dripapplication.data.remote.model

import com.drip.dripapplication.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "Name")
    val name: String,
    @Json(name = "Age")
    val age: Int,
    @Json(name = "Description")
    val description: String,
    @Json(name = "ImgSrc")
    val images: List<String>,
    @Json(name = "Tags")
    val tags: List<String>
){
    fun toDomainModel() = User(name, age, description, images, tags)
}
