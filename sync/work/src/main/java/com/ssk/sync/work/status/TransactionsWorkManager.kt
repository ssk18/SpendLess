package com.ssk.sync.work.status

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ssk.core.domain.repository.ITransactionsRepository
import com.ssk.core.domain.repository.SyncManager
import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import com.ssk.sync.work.initializer.SYNC_WORK_NAME
import com.ssk.sync.work.workers.RecurringTransactionWorker
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.pow

class TransactionsWorkManager(
    private val context: Context,
    private val transactionsRepository: ITransactionsRepository
) : SyncManager {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override val isSyncing: Flow<Boolean>
        get() = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun observeRecurringTransactions() {
        coroutineScope.launch {
            transactionsRepository.observeRecurringTransactions()
                .handleRecurringTransactionsCount()
                .shareIn(
                    coroutineScope,
                    started = SharingStarted.Lazily,
                    replay = 1
                )
                .collect {
                    Timber.d("Recurring transactions count: $it")
                    updateWorkScheduling(it)
                }
        }
    }

    private fun Flow<Result<Int, DataError>>.handleRecurringTransactionsCount(): Flow<Int> {
        return this.map { result ->
            when (result) {
                is Result.Error -> kotlin.Result.failure(Exception(result.error.toString()))
                is Result.Success -> kotlin.Result.success(result.data)
            }
        }
            .retryWhen { cause, attempt ->
                val retryDelay = (2.0.pow(attempt.toDouble()) * 1000).toLong().coerceAtMost(30_000)
                delay(retryDelay)
                attempt < 5 && cause !is CancellationException
            }
            .mapNotNull {
                it.getOrNull()
            }
            .onStart { emit(0) }
            .distinctUntilChanged()
            .onEach {
                Timber.d("Recurring transactions count: $it")
            }
    }

    private fun updateWorkScheduling(count: Int) {
        if (count > 0) {
            scheduleWork()
        } else {
            cancelWork()
        }
    }

    private fun scheduleWork() {
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            RecurringTransactionWorker.startRecurringTransactionWork()
        )
    }

    private fun cancelWork() {
        WorkManager.getInstance(context).cancelUniqueWork(SYNC_WORK_NAME)
    }
}

fun List<WorkInfo>.anyRunning() = any {
    it.state == WorkInfo.State.RUNNING
}