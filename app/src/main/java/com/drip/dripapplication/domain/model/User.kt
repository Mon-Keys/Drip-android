package com.drip.dripapplication.domain.model

data class User(
    val name: String,
    val age: Int,
    val description: String,
    val images: List<String>,
    val tags: List<String>
)
