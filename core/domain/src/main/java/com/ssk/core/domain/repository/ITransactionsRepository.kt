package com.ssk.core.domain.repository

import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface ITransactionsRepository {
    fun getTransactionByUser(userId: Long): Flow<Result<List<Transaction>, DataError>>
    suspend fun saveTransaction(transaction: Transaction): Result<Unit, DataError>
    suspend fun removeTransaction(transaction: Transaction)
    suspend fun getRecurringTransactions(): Result<List<Transaction>, DataError>
    suspend fun getMostPopularCategory(userId: Long): Result<TransactionType?, DataError>
    suspend fun getLargestTransaction(userId: Long): Result<Transaction?, DataError>
    suspend fun getPreviousWeekTransactions(userId: Long): Result<List<Transaction>, DataError>
    suspend fun observeRecurringTransactions(): Flow<Result<Int, DataError>>
}