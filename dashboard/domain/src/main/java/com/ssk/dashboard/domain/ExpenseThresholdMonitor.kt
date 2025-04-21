package com.ssk.dashboard.domain

import com.ssk.core.domain.utils.DataError
import com.ssk.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface ExpenseThresholdMonitor {
    fun observeUserExpenseThreshold(userId: Long, threshold: Float): Flow<Result<Boolean, DataError>>
    suspend fun checkIfThresholdExceeded(userId: Long, threshold: Float): Result<Boolean, DataError>
}