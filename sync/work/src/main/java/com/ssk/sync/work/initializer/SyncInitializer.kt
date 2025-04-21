package com.ssk.sync.work.initializer

import com.ssk.core.domain.repository.SyncManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Sync: KoinComponent {
    fun initialize() {
        val syncManager: SyncManager by inject()
        syncManager.observeRecurringTransactions()
    }
}

internal const val SYNC_WORK_NAME =  "recurring_transaction_work"