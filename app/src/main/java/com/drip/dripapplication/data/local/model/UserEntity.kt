package com.drip.dripapplication.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drip.dripapplication.domain.model.User

@Entity
data class UserEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val age: Int,
    val description: String?,
    val images: List<String>,
    val tags: List<String>?
){
    fun toDomainModel() = User(id, name, age, description ?: "", images, tags ?: emptyList())
}