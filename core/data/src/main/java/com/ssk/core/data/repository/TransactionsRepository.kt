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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.Instant
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs

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

    override suspend fun getMostPopularCategory(userId: Long): Result<TransactionType?, DataError> {
        return try {
            val transactions =
                transactionDao.getExpenseTransactions(userId).firstOrNull() ?: emptyList()
            val expenseTransactions = transactions.map { it.toDomain() }
                .filter { it.transactionType != TransactionType.INCOME }

            val categoryCount = expenseTransactions
                .groupBy { it.transactionType }
                .mapValues { it.value.size }

            val maxCount = categoryCount.maxOfOrNull {
                it.value
            }

            val mostPopularCategories = categoryCount.filterValues { it == maxCount }

            Timber.d("Most popular categories: $mostPopularCategories")
            val mostPopularCategory: TransactionType? = if (mostPopularCategories.isNotEmpty()) {
                // Always decide based on total amount spent for each category
                mostPopularCategories.keys.maxByOrNull { type ->
                    // Calculate the total absolute amount for this category
                    val totalAmount = expenseTransactions
                        .filter { it.transactionType == type }
                        .sumOf { Math.abs(it.amount.toDouble()) }
                    Timber.d("Category $type total amount: $totalAmount")
                    totalAmount
                }
            } else {
                null
            }
            Timber.d("Most popular category selected: $mostPopularCategory")
            Result.Success(mostPopularCategory)

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.e(e, "Error fetching largest transaction")
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun getLargestTransaction(userId: Long): Result<Transaction?, DataError> {
        return try {
            val transactions =
                transactionDao.getExpenseTransactions(userId).firstOrNull() ?: emptyList()
            val decryptedTransactions = transactions.map { it.toDomain() }
            // Use maxByOrNull with abs() to find largest expense by magnitude
            val largestTransaction =
                decryptedTransactions.maxByOrNull { abs(it.amount) }
            Timber.d("Largest transaction found: ${largestTransaction?.title}, amount: ${largestTransaction?.amount}")
            Result.Success(largestTransaction)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.e(e, "Error fetching largest transaction")
            Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun getPreviousWeekTransactions(userId: Long): Result<List<Transaction>, DataError> {
        val (lastMonday, lastSunday) = InstantFormatter.getPreviousWeekRange()

        try {
            // Get all transactions for the user
            val transactionsResult = getTransactionByUser(userId).firstOrNull()

            return when (transactionsResult) {
                is Result.Success -> {
                    val previousWeekTransactions = transactionsResult.data
                        .filter { transaction ->
                            transaction.transactionType != TransactionType.INCOME
                        }
                        .filter { transaction ->
                            val transactionDate = Instant.ofEpochMilli(transaction.transactionDate)
                            transactionDate.isAfter(lastMonday) &&
                                    (transactionDate.isBefore(lastSunday) || transactionDate == lastSunday)
                        }
                    Result.Success(previousWeekTransactions)
                }

                is Result.Error -> transactionsResult
                null -> Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.e(e, "Error fetching previous week transactions")
            return Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    override suspend fun observeRecurringTransactions(): Flow<Result<Int, DataError>> {
        return transactionDao.observeRecurringTransactions()
            .map { Result.Success(it) }
            .catch { e ->
                if (e is CancellationException) throw e
                Timber.e(e, "Error fetching largest transaction")
                Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
            }
            .flowOn(Dispatchers.IO)
    }

}