package com.drip.dripapplication.data.remote.model

import com.drip.dripapplication.domain.model.SignupResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupResponseDto(
    val id: Long,
    val email: String,
){
    fun toDomainModel() = SignupResponse(id, email)
}
