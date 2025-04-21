package com.ssk.spendless

import android.app.Application
import com.ssk.auth.data.di.userDataValidatorModule
import com.ssk.auth.presentation.di.authViewModelModule
import com.ssk.core.data.di.coreDataModule
import com.ssk.core.database.di.dataStoreModule
import com.ssk.core.database.di.databaseModule
import com.ssk.dashboard.data.di.csvDataModule
import com.ssk.dashboard.presentation.di.dashboardModule
import com.ssk.settings.presentation.di.settingsViewModelModule
import com.ssk.spendless.di.appModule
import com.ssk.sync.work.di.syncKoinModule
import com.ssk.sync.work.initializer.Sync
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class SpendLessApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Sync.initialize()
        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            workManagerFactory()
            modules(
                databaseModule,
                dataStoreModule,
                coreDataModule,
                userDataValidatorModule,
                authViewModelModule,
                dashboardModule,
                appModule,
                settingsViewModelModule,
                csvDataModule,
                syncKoinModule
            )
        }
    }
}