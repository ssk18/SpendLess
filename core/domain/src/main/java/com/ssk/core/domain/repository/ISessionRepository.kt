package com.ssk.core.domain.repository

import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.model.SessionExpiryDuration

interface ISessionRepository {
    suspend fun logIn(username: String)
    suspend fun logOut()
    fun getLoggedInUsername(): String?
    fun isUserLoggedIn(): Boolean
    fun startSession(sessionExpiryDuration: SessionExpiryDuration)
    fun isSessionExpired(): Boolean
    fun setPinLockTimestamp(lockedOutDuration: LockedOutDuration)
    fun getPinLockRemainingTime(): Int
    fun restorePinLock()
    fun refreshSession()
}