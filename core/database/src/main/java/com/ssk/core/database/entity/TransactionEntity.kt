package com.ssk.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: Long,
    val title: String,
    val amount: String,
    val repeatType: String,
    val transactionType: String,
    val note: String?,
    val transactionDate: String,
)
