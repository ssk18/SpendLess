package com.ssk.core.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssk.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM user_info WHERE username=:userName")
    suspend fun getUser(userName: String): UserEntity?

    @Query("SELECT * FROM user_info")
    fun getAllUsers(): Flow<List<UserEntity>>
}