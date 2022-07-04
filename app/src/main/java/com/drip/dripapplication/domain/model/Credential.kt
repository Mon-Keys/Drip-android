package com.drip.dripapplication.domain.model

import com.drip.dripapplication.data.remote.model.CredentialDto

data class Credential(
    val email: String,
    val password: String
){
    fun toRepModel() = CredentialDto(email, password)
}
