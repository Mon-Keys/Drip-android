package com.drip.dripapplication.data.remote.model

import com.drip.dripapplication.domain.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.FormBody

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Long,
    val name: String,
    val age: Int,
    val description: String?,
    @Json(name = "imgs")
    val images: List<String>,
    val tags: List<String>?
){
    fun toDomainModel() = User(id, name, age, description ?: "", images, tags ?: emptyList())
}
