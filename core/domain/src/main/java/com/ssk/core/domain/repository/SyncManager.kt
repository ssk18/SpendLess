package com.ssk.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
    fun observeRecurringTransactions()
}