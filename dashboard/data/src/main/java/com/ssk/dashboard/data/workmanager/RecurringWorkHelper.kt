package com.ssk.dashboard.data.workmanager

import androidx.work.Constraints
import java.time.Duration
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

val recurringTransactionWorkConstraints = Constraints.Builder()
    .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
    .setRequiresBatteryNotLow(true)
    .build()

fun setInitialDelay(): Long {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    val nextRunTime = currentTime.with(LocalTime.of(2, 0, 0))

    val adjustedTime = if (currentTime.isAfter(nextRunTime)) {
        nextRunTime.plusDays(1)
    } else {
        nextRunTime
    }

    return Duration.between(currentTime, adjustedTime).toMillis()
}
