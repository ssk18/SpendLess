package com.ssk.core.data.repository

import com.ssk.core.database.dao.TransactionDao
import com.ssk.core.database.mapper.toDomain
import com.ssk.core.database.mapper.toEntity
import com.ssk.core.domain.model.Expense
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.Instant
import kotlin.coroutines.cancellation.CancellationException

class TransactionsRepository(
    private val transactionDao: TransactionDao,
) : ITransactionsRepository {
    override fun getTransactionByUser(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        return transactionDao.getTransactionsByUser(userId)
            .map { transactions ->
                Result.Success(transactions.map { it.toDomain() })
            }
            .catch { e ->
                if (e is CancellationException) throw e
                Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun saveTransaction(transaction: Transaction): Result<Unit, DataError> {
        return try {
            val transactionEntity = transaction.toEntity()
            transactionDao.upsertTransaction(transactionEntity)
            Result.Success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun removeTransaction(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }

    override suspend fun getRecurringTransactions(): Result<List<Transaction>, DataError> {
        return try {
            val recurringTransactions =
                transactionDao.getRecurringTransactions().map { it.toDomain() }
            val today = Instant.now().toEpochMilli()

            for (transaction in recurringTransactions) {
                if (transaction.shouldRepeat(today)) {
                    val newTransaction = transaction.copy(transactionDate = today)
                    transactionDao.upsertTransaction(newTransaction.toEntity())
                }
            }
            Result.Success(recurringTransactions) as Result<List<Transaction>, DataError>
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun getMostPopularCategory(userId: Long): Flow<Result<TransactionType?, DataError>> {
        return transactionDao.getExpenseTransactions(userId)
            .map { categories ->
                val categories = categories.mapNotNull { category ->
                    Expense.entries.find { it.type == category }
                }

                val mostPopularCategory = categories
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key
                Result.Success(mostPopularCategory) as Result<TransactionType?, DataError>
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

}