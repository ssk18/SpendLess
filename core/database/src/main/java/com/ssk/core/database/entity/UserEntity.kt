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
    @ColumnInfo(name = "pin_code") val pinCode: ByteArray,
    val expensesFormat: String,
    val currency: String,
    val decimalSeparator: String,
    val thousandsSeparator: String,
    val biometricsPrompt: String,
    val sessionExpiryDuration: String,
    val lockedOutDuration: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (userId != other.userId) return false
        if (username != other.username) return false
        if (!pinCode.contentEquals(other.pinCode)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + pinCode.contentHashCode()
        return result
    }
}