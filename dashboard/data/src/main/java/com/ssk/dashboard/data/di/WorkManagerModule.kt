package com.ssk.dashboard.data.di

import com.ssk.dashboard.data.workmanager.TransactionsWorkManager
import com.ssk.dashboard.domain.SyncManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.dsl.module

val workManagerModule = module {
    single { KoinWorkerFactory(getKoin()) }
    single<SyncManager> { TransactionsWorkManager(androidContext()) }
}