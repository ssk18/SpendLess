package com.ssk.spendless.di

import com.ssk.dashboard.data.workmanager.RecurringTransactionWorker
import com.ssk.spendless.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    workerOf(::RecurringTransactionWorker)
}