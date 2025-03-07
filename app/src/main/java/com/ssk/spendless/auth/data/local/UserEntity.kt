package com.ssk.spendless.auth.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey (autoGenerate = true) val id: Int,
    @ColumnInfo(name = "username") val username: String,
)
