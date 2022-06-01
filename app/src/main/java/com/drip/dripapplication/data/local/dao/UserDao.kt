package com.drip.dripapplication.data.local.dao

import androidx.room.*
import com.drip.dripapplication.data.local.model.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

//    @Query("SELECT * FROM user WHERE id = :id ")
//    fun findUserById(id: Long): UserEntity?

    @Delete
    fun deleteUser(user: UserEntity)
}