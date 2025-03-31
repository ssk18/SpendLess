package com.ssk.spendless.auth.data.di

import android.content.Context
import androidx.room.Room
import com.ssk.spendless.auth.data.local.AuthDatabase
import com.ssk.spendless.auth.domain.UserDataValidator
import com.ssk.spendless.core.data.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.MessageDigest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAuthDatabase(
        @ApplicationContext applicationContext: Context,
        cryptoManager: CryptoManager
    ): AuthDatabase {
        val secretKey = "db_secret_key"

        val secretBytes = secretKey.toByteArray()

        val encryptedKey = try {
            val secretKeyFile = File(applicationContext.filesDir, "secret_key.enc")
            if (!secretKeyFile.exists()) {
                cryptoManager.encrypt(secretBytes, secretKeyFile.outputStream())
            } else {
                cryptoManager.encrypt(secretBytes, ByteArrayOutputStream())
            }
            MessageDigest.getInstance("SHA-256").digest(secretBytes)
        } catch (e: Exception) {
            secretBytes
        }

        return Room.databaseBuilder(
            applicationContext,
            AuthDatabase::class.java,
            "auth_database"
        )
          //  .openHelperFactory(SupportFactory(encryptedKey))
            .build()
    }

    @Provides
    fun provideAuthDao(authDatabase: AuthDatabase) = authDatabase.userDao()

    @Provides
    @Singleton
    fun provideUsernameValidator(): UserDataValidator {
        return UserDataValidator()
    }
}