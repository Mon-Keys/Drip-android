package com.drip.dripapplication.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CredentialDto(
    val email: String,
    val password: String
)
