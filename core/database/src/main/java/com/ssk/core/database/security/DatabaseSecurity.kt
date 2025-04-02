package com.ssk.core.database.security

import android.content.Context
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.security.SecureRandom
import java.util.Base64

object DatabaseSecurity {
    private const val DATABASE_PASSPHRASE = "passphrase_db"
    private const val SECURED_DATASTORE_FILE_NAME = "secure_datastore"
    private const val PASSPHRASE_LENGTH = 32

    private val Context.secureDataStore by
    dataStore(
        fileName = SECURED_DATASTORE_FILE_NAME,
        serializer = UserPreferenceSerializer,
    )

    /**
     * Generates a secure random passphrase for database encryption
     */
    private fun generateSecurePassphrase(): String {
        val random = SecureRandom()
        val bytes = ByteArray(PASSPHRASE_LENGTH)
        random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun getDatabasePassphrase(context: Context): String = runBlocking {
        val preferences = context.secureDataStore.data.first()
        preferences.databasePassphrase ?: run {
            val newPassphrase = generateSecurePassphrase()
            saveDatabasePassphrase(context, newPassphrase)
            newPassphrase
        }
    }


    /**
     * Saves the database passphrase to DataStore
     */
    private suspend fun saveDatabasePassphrase(context: Context, passphrase: String) {
        context.secureDataStore.updateData { currentPreferences ->
            currentPreferences.copy(databasePassphrase = passphrase)
        }
    }

    /**
     * Observe database passphrase changes as a Flow
     */
    fun observeDatabasePassphrase(context: Context): Flow<String?> {
        return context.secureDataStore.data.map { preferences ->
            preferences.databasePassphrase
        }
    }

    /**
     * Regenerates the database passphrase and updates it in DataStore
     * Useful for security rotations or if passphrase is compromised
     */
    suspend fun regenerateDatabasePassphrase(context: Context): String {
        val newPassphrase = generateSecurePassphrase()
        saveDatabasePassphrase(context, newPassphrase)
        return newPassphrase
    }
}