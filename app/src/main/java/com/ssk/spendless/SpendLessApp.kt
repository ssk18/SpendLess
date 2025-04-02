package com.ssk.spendless

import android.app.Application
import com.ssk.auth.data.di.userDataValidatorModule
import com.ssk.auth.presentation.di.authViewModelModule
import com.ssk.core.data.di.coreDataModule
import com.ssk.core.database.di.dataStoreModule
import com.ssk.core.database.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class SpendLessApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SpendLessApp)
            modules(
                databaseModule,
                dataStoreModule,
                coreDataModule,
                userDataValidatorModule,
                authViewModelModule
            )
        }
    }
}