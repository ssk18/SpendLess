package com.ssk.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ssk.core.database.entity.TransactionEntity
import com.ssk.core.domain.model.RepeatType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    fun getTransactionsByUser(userId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE repeatType != :repeatType")
    suspend fun getRecurringTransactions(repeatType: RepeatType = RepeatType.NOT_REPEAT): List<TransactionEntity>

}