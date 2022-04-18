package com.drip.dripapplication.data.remote.model

import com.drip.dripapplication.domain.model.Cridential
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CridentialDto(
    val email: String,
    val password: String
)