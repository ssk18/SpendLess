package com.ssk.spendless.auth.data.local

import androidx.room.Dao
import androidx.room.Upsert


@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

}