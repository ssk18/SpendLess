package com.ssk.dashboard.data.expense_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ssk.dashboard.domain.ExpenseAlarmScheduler
import java.util.Calendar

class ExpenseAlarmSchedulerImpl(
    private val context: Context
) : ExpenseAlarmScheduler {

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleExpenseCheck() {
        val alarmIntent = Intent(context, ExpenseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            EXPENSE_ALARM_REQUEST_CODE,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 30)

            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 20,
            pendingIntent
        )
    }

    override fun cancelExpenseCheck() {
        val intent = Intent(context, ExpenseAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            EXPENSE_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    companion object {
        const val EXPENSE_ALARM_REQUEST_CODE = 1001
    }
}