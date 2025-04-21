package com.ssk.dashboard.data.expense_alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ssk.dashboard.domain.ExpenseAlarmScheduler
import org.koin.java.KoinJavaComponent.inject

class BootCompletedReceiver: BroadcastReceiver() {

    private val expenseAlarmScheduler by inject<ExpenseAlarmScheduler>(ExpenseAlarmScheduler::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            expenseAlarmScheduler.scheduleExpenseCheck()
        }
    }
}