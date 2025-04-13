package com.ssk.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssk.core.database.dao.TransactionDao
import com.ssk.core.database.dao.UserDao
import com.ssk.core.database.entity.TransactionEntity
import com.ssk.core.database.entity.UserEntity
import com.ssk.core.database.mapper.TransactionConverter

@Database(entities = [UserEntity::class, TransactionEntity::class], version = 2)
@TypeConverters(TransactionConverter::class)
abstract class SpendLessDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val transactionDao: TransactionDao
}