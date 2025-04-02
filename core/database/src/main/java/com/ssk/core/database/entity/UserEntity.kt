package com.ssk.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_info",
    indices = [
        Index(value = ["username"], unique = true),
        Index(value = ["userId"])
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "pin_code") val pinCode: String
)