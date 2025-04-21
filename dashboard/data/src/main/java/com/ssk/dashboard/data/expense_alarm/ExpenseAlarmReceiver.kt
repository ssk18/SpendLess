package com.ssk.dashboard.data.expense_alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.domain.repository.IUserRepository
import com.ssk.core.domain.utils.Result
import com.ssk.dashboard.domain.ExpenseNotificationManager
import com.ssk.dashboard.domain.ExpenseThresholdMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class ExpenseAlarmReceiver: BroadcastReceiver(), KoinComponent {

    private val expenseThresholdMonitor: ExpenseThresholdMonitor by inject()
    private val userRepository: IUserRepository by inject()
    private val notificationManager: ExpenseNotificationManager by inject()
    private val sessionRepository: ISessionRepository by inject()
    private val threshold = 1300f

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            Timber.e("Received null context in ExpenseAlarmReceiver")
            return
        }
        
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            checkExpenseThreshold()
        }
    }

    private suspend fun checkExpenseThreshold() {
        try {
            val user = sessionRepository.getLoggedInUsername()
            if (user.isNullOrEmpty()) {
                Timber.d("No user logged in, skipping expense check")
                return
            }
            val userResult = userRepository.getUser(user)
            if (userResult is Result.Error) {
                Timber.e("Failed to get user: ${userResult.error}")
                return
            }

            val userDetails = (userResult as Result.Success).data
            val userId = userDetails.userId
            
            if (userId == null) {
                Timber.e("User has null ID, skipping expense check")
                return
            }

            when (val result = expenseThresholdMonitor.checkIfThresholdExceeded(userId, threshold)) {
                is Result.Success -> {
                    if (result.data) {
                        notificationManager.showExpenseAlert(threshold)
                    }
                }
                is Result.Error -> {
                    Timber.e("Error checking threshold: ${result.error}")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error in expense threshold check")
        }
    }
}