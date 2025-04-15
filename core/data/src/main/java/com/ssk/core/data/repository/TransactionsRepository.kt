package com.ssk.core.data.repository

import com.ssk.core.database.dao.TransactionDao
import com.ssk.core.database.mapper.toDomain
import com.ssk.core.database.mapper.toEntity
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.InstantFormatter
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
                    TransactionType.entries.find { it.type == category.transactionType }
                }
                    .filter { it != TransactionType.INCOME }

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

    override suspend fun getLargestTransaction(userId: Long): Flow<Result<Transaction, DataError>> {
        return getTransactionByUser(userId)
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        // Filter out income transactions and get expenses only
                        val expenses = result.data.filter { transaction ->
                            transaction.transactionType != TransactionType.INCOME
                        }
                        
                        if (expenses.isEmpty()) {
                            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                        } else {
                            // Find the transaction with the highest absolute amount (biggest expense)
                            val largestTransaction = expenses.maxByOrNull { transaction ->
                                // Use absolute value to properly compare expense amounts
                                Math.abs(transaction.amount)
                            }
                            
                            largestTransaction?.let {
                                Result.Success(it)
                            } ?: Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
                        }
                    }

                    is Result.Error -> result
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getPreviousWeekTransactions(userId: Long): Flow<Result<List<Transaction>, DataError>> {
        val (lastMonday, lastSunday) = InstantFormatter.getPreviousWeekRange()

        return getTransactionByUser(userId)
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        val previousWeekTransactions = result.data
                            .filter { transaction ->
                                transaction.transactionType != TransactionType.INCOME
                            }
                            .filter { transaction ->
                                val transactionDate =
                                    Instant.ofEpochMilli(transaction.transactionDate)
                                transactionDate.isAfter(lastMonday) &&
                                        (transactionDate.isBefore(lastSunday) || transactionDate == lastSunday)
                            }
                        Result.Success(previousWeekTransactions)
                    }

                    is Result.Error -> result
                }
            }
            .catch { e ->
                if (e is CancellationException) throw e
                emit(Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR))
            }
            .flowOn(Dispatchers.IO)
    }

}