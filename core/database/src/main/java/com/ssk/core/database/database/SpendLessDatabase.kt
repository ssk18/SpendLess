package com.ssk.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssk.core.database.dao.UserDao
import com.ssk.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
abstract class SpendLessDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}