package com.ssk.dashboard.domain

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
    fun requestRecurringTransactions()
}