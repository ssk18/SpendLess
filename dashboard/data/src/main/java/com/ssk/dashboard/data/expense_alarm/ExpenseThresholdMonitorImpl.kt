package com.ssk.dashboard.data.expense_alarm

import android.os.Build
import com.ssk.core.domain.model.Transaction
import com.ssk.core.domain.model.TransactionType
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import com.ssk.dashboard.domain.ExpenseThresholdMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.absoluteValue

class ExpenseThresholdMonitorImpl(
    private val transactionsRepository: ITransactionsRepository,
) : ExpenseThresholdMonitor {
    /**
     * Observe the user's monthly expenses and check if they exceed the given threshold.
     *
     * @param userId The ID of the user.
     * @param threshold The expense threshold to check against.
     * @return A flow that emits true if the user's monthly expenses exceed the threshold, false otherwise.
     */
    override fun observeUserExpenseThreshold(
        userId: Long,
        threshold: Float
    ): Flow<Result<Boolean, DataError>> {
        return transactionsRepository.getTransactionByUser(userId)
            .map { result ->
                when (result) {
                    is Result.Error -> result as Result<Boolean, DataError>
                    is Result.Success -> {
                        val transactions = result.data
                        val monthlyExpenses = calculateMonthlyExpenses(transactions)
                        if (monthlyExpenses > threshold) {
                            Result.Success(true)
                        } else {
                            Result.Success(false)
                        }
                    }
                }
            }
    }

    override suspend fun checkIfThresholdExceeded(
        userId: Long,
        threshold: Float
    ): Result<Boolean, DataError> {
        try {
            // Get all transactions for the user
            val transactionsResult = transactionsRepository.getTransactionByUser(userId).firstOrNull()
                ?: return Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)

            // If there was an error getting transactions, pass it along
            if (transactionsResult is Result.Error) {
                return transactionsResult as Result<Boolean, DataError>
            }

            // Calculate total expenses for current month
            val transactions = (transactionsResult as Result.Success).data
            val currentMonthExpenses = calculateMonthlyExpenses(transactions)

            // Return whether expenses exceed threshold
            return Result.Success(currentMonthExpenses > threshold)

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.e(e, "Error checking expense threshold: ${e.message}")
            return Result.Error(DataError.Local.UNKNOWN_DATABASE_ERROR)
        }
    }

    /**
     * Calculate the user's monthly expenses based on their transactions.
     *
     * @param transactions The list of transactions to calculate the monthly expenses from.
     * @return The total monthly expenses.
     */
    private fun calculateMonthlyExpenses(transactions: List<Transaction>): Float {
        val now = LocalDate.now()

        return transactions.filter { transaction ->
            val transactionDate =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    LocalDate.ofInstant(
                        Instant.ofEpochMilli(transaction.transactionDate),
                        ZoneId.systemDefault()
                    )
                } else {
                    TODO("VERSION.SDK_INT < UPSIDE_DOWN_CAKE")
                }
            transaction.transactionType != TransactionType.INCOME &&
                    transactionDate.year == now.year &&
                    transactionDate.month == now.month
        }
            .sumOf {
                it.amount.toDouble()
            }
            .toFloat()
            .absoluteValue
    }
}