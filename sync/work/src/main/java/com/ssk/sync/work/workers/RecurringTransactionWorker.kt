package com.ssk.sync.work.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ssk.core.domain.repository.ITransactionsRepository
import java.time.Duration

class RecurringTransactionWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val transactionsRepository: ITransactionsRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        transactionsRepository.getRecurringTransactions()
        return Result.success()
    }

    companion object {
        fun startRecurringTransactionWork() =
            PeriodicWorkRequestBuilder<RecurringTransactionWorker>(
                Duration.ofHours(24),
                Duration.ofHours(1)
            ).setInitialDelay(Duration.ofMillis(setInitialDelay()))
                .setConstraints(recurringTransactionWorkConstraints)
                .build()
    }
}