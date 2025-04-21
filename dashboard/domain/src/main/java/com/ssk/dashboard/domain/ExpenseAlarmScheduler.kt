package com.ssk.dashboard.domain

interface ExpenseAlarmScheduler {
    fun scheduleExpenseCheck()
    fun cancelExpenseCheck()
}