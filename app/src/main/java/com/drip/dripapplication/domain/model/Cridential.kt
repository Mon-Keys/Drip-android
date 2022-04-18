package com.drip.dripapplication.domain.model

import com.drip.dripapplication.data.remote.model.CridentialDto

data class Cridential(
    val login: String,
    val password: String
){
    fun toRepModel() = CridentialDto(login, password)
}
