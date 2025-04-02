package com.ssk.spendless.auth.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssk.core.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AuthDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}