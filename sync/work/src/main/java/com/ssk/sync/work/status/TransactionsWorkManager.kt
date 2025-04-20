package com.ssk.sync.work.status

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ssk.core.domain.repository.SyncManager
import com.ssk.sync.work.initializer.SYNC_WORK_NAME
import com.ssk.sync.work.workers.RecurringTransactionWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map

class TransactionsWorkManager(
    private val context: Context
): SyncManager {
    override val isSyncing: Flow<Boolean>
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun requestRecurringTransactions() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RecurringTransactionWorker.startRecurringTransactionWork()
        )
    }
}

fun List<WorkInfo>.anyRunning() = any {
    it.state == WorkInfo.State.RUNNING
}