package com.ssk.core.database.di

import androidx.room.Room
import com.ssk.core.database.database.SpendLessDatabase
import com.ssk.core.database.security.DatabaseSecurity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        val dbName = "spendless_database.db"
        val builder = Room.databaseBuilder(
            androidApplication(),
            SpendLessDatabase::class.java,
            dbName
        )

        val passphrase = DatabaseSecurity.getDatabasePassphrase(androidApplication())
        val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))
        builder
           // .openHelperFactory(factory)
            .build()
    }
    single { get<SpendLessDatabase>().userDao }
}