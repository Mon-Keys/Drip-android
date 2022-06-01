package com.drip.dripapplication.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchDto(
    val match: Boolean
)