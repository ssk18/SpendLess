package com.ssk.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssk.core.database.dao.UserDao
import com.ssk.core.database.entity.UserEntity
import com.ssk.core.database.typecoverters.ByteArrayConverter

@Database(
    entities = [
        UserEntity::class
    ],
    version = 1
)
@TypeConverters(ByteArrayConverter::class)
abstract class SpendLessDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}