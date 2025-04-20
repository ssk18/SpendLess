package com.ssk.dashboard.data.workmanager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ssk.dashboard.domain.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

class TransactionsWorkManager(
    private val context: Context
): SyncManager {
    override val isSyncing: Flow<Boolean>
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow("recurring_transaction_work")
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun requestRecurringTransactions() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "recurring_transaction_work",
            ExistingPeriodicWorkPolicy.KEEP,
            RecurringTransactionWorker.startRecurringTransactionWork()
        )
    }
}

fun List<WorkInfo>.anyRunning() = any {
    it.state == WorkInfo.State.RUNNING
}