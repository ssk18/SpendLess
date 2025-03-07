package com.ssk.spendless.auth.data.di

import android.content.Context
import androidx.room.Room
import com.ssk.spendless.auth.data.local.AuthDatabase
import com.ssk.spendless.auth.domain.UserDataValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAuthDatabase(@ApplicationContext applicationContext: Context): AuthDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AuthDatabase::class.java,
            "auth_database"
        ).build()
    }

    @Provides
    fun provideAuthDao(authDatabase: AuthDatabase) = authDatabase.userDao()

    @Provides
    @Singleton
    fun provideUsernameValidator(): UserDataValidator {
        return UserDataValidator()
    }
}