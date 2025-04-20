package com.ssk.sync.work.initializer

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.ssk.sync.work.workers.RecurringTransactionWorker

object Sync {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).apply {
            enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                RecurringTransactionWorker.startRecurringTransactionWork(),
            )
        }
    }
}

internal const val SYNC_WORK_NAME =  "recurring_transaction_work"