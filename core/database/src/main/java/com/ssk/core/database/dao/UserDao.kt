package com.ssk.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ssk.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    @Query("SELECT * FROM user_info WHERE username=:userName")
    suspend fun getUser(userName: String): UserEntity?
    
    @Query("SELECT * FROM user_info WHERE userId=:userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM user_info")
    fun getAllUsers(): Flow<List<UserEntity>>
}