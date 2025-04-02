package com.ssk.spendless.auth.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ssk.core.database.entity.UserEntity


@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    @Query("SELECT * FROM userentity WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

}