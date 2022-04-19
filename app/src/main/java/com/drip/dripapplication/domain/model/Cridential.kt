package com.drip.dripapplication.domain.model

import com.drip.dripapplication.data.remote.model.CridentialDto

data class Cridential(
    val email: String,
    val password: String
){
    fun toRepModel() = CridentialDto(email, password)
}
