package com.drip.dripapplication.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWrapper<T>(
    val status: Int,
    val body: T?
)
