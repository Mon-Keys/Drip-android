package com.drip.dripapplication.domain.model

import android.os.Parcelable
import com.drip.dripapplication.data.remote.model.UserDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val description: String,
    val images: List<String>,
    val tags: List<String>
) : Parcelable {
    fun toRepModel() = UserDto(id, name, age, description ?: "", images, tags ?: emptyList())

}
