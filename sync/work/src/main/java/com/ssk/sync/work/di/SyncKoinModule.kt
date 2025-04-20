package com.ssk.sync.work.di

import com.ssk.core.domain.repository.SyncManager
import com.ssk.sync.work.status.TransactionsWorkManager
import com.ssk.sync.work.workers.RecurringTransactionWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val syncKoinModule = module {
    singleOf(::TransactionsWorkManager) bind SyncManager::class
    workerOf(::RecurringTransactionWorker)
}