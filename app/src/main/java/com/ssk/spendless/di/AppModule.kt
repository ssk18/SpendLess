package com.ssk.spendless.di

import com.ssk.dashboard.domain.ExpenseNotificationManager
import com.ssk.spendless.MainViewModel
import com.ssk.spendless.notifications.ExpenseNotificationManagerImpl
import com.ssk.sync.work.workers.RecurringTransactionWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    workerOf(::RecurringTransactionWorker)

    single<ExpenseNotificationManager> { ExpenseNotificationManagerImpl(androidContext()) }
}