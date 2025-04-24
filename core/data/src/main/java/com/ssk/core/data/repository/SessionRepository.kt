package com.ssk.core.data.repository

import android.os.SystemClock
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.model.SessionExpiryDuration
import com.ssk.core.domain.repository.ISessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class SessionRepository(
    private val dataStore: DataStore<Preferences>
) : ISessionRepository {

    companion object {
        private const val KEY_SESSION_TIMESTAMP = "session_timestamp"
        private const val KEY_SESSION_EXPIRY_DURATION = "session_expiry_duration"
        private const val USERNAME_KEY = "username"
        private const val FIFTEEN_SEC_MILLIS = 15 * 1000L
        private const val THIRTY_SEC_MILLIS = 30 * 1000L
        private const val MINUTE_IN_MILLIS = 60 * 1000L
        private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
        private const val KEY_PIN_LOCK_TIMESTAMP = "pin_lock_timestamp"
        private const val KEY_PIN_LOCK_DURATION = "pin_lock_duration"
    }

    override suspend fun logIn(username: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(USERNAME_KEY)] = username
        }
    }

    override suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(USERNAME_KEY))
        }
    }

    override fun getLoggedInUsername(): String? {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[stringPreferencesKey(USERNAME_KEY)]
            }.firstOrNull()
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return getLoggedInUsername() != null
    }

    override fun startSession(sessionExpiryDuration: SessionExpiryDuration) {
        val duration = when (sessionExpiryDuration) {
            SessionExpiryDuration.FIVE_MIN -> 5 * MINUTE_IN_MILLIS
            SessionExpiryDuration.FIFTEEN_MIN -> 15 * MINUTE_IN_MILLIS
            SessionExpiryDuration.THIRTY_MIN -> 30 * MINUTE_IN_MILLIS
            SessionExpiryDuration.HOUR -> HOUR_IN_MILLIS
        }

        runBlocking {
            dataStore.edit { preferences ->
                preferences[longPreferencesKey(KEY_SESSION_TIMESTAMP)] =
                    SystemClock.elapsedRealtime()
                preferences[longPreferencesKey(KEY_SESSION_EXPIRY_DURATION)] = duration
            }
        }
    }

    override fun isSessionExpired(): Boolean {
        val currentTime = SystemClock.elapsedRealtime()
        val sessionData = runBlocking {
            dataStore.data.map { preferences ->
                Pair(
                    preferences[longPreferencesKey(KEY_SESSION_TIMESTAMP)],
                    preferences[longPreferencesKey(KEY_SESSION_EXPIRY_DURATION)]
                )
            }.first()
        }

        val sessionStartTime = sessionData.first ?: 0L
        val sessionExpiryDuration = sessionData.second ?: 0L

        return currentTime - sessionStartTime > sessionExpiryDuration
    }

    override fun setPinLockTimestamp(lockedOutDuration: LockedOutDuration) {
        val lockedOutDurationMillis = when (lockedOutDuration) {
            LockedOutDuration.FIFTEEN_SEC -> FIFTEEN_SEC_MILLIS
            LockedOutDuration.THIRTY_SEC -> THIRTY_SEC_MILLIS
            LockedOutDuration.ONE_MIN -> 1 * MINUTE_IN_MILLIS
            LockedOutDuration.FIVE_MIN -> 5 * MINUTE_IN_MILLIS
        }

        runBlocking {
            dataStore.edit { preferences ->
                preferences[longPreferencesKey(KEY_PIN_LOCK_TIMESTAMP)] = SystemClock.elapsedRealtime()
                preferences[longPreferencesKey(KEY_PIN_LOCK_DURATION)] = lockedOutDurationMillis
            }
        }
    }

    override fun getPinLockRemainingTime(): Int {
        val pinLockData = runBlocking {
            dataStore.data.map { preferences ->
                Pair(
                    preferences[longPreferencesKey(KEY_PIN_LOCK_TIMESTAMP)],
                    preferences[longPreferencesKey(KEY_PIN_LOCK_DURATION)]
                )
            }.first()
        }
        val lockedOutDuration = pinLockData.second ?: 0L
        val lockStartTime = pinLockData.first ?: 0L

        if (lockStartTime == 0L || lockedOutDuration == 0L) {
            return 0
        }

        val elpasedTime = SystemClock.elapsedRealtime() - lockStartTime
        return ((lockedOutDuration - elpasedTime).coerceAtLeast(0L) / 1000).toInt()
    }

    override fun restorePinLock() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences.remove(longPreferencesKey(KEY_PIN_LOCK_TIMESTAMP))
                preferences.remove(longPreferencesKey(KEY_PIN_LOCK_DURATION))
            }
        }
    }

    override fun refreshSession() {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[longPreferencesKey(KEY_SESSION_TIMESTAMP)] = SystemClock.elapsedRealtime()
            }
        }
    }
}