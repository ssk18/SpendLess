package com.ssk.spendless.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ssk.dashboard.domain.ExpenseNotificationManager
import com.ssk.spendless.MainActivity
import com.ssk.spendless.R
import timber.log.Timber

class ExpenseNotificationManagerImpl(
    private val context: Context
): ExpenseNotificationManager {

    override fun showExpenseAlert(thresholdAmount: Float) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
            val pendingIntent = createPendingIntent()
            val notification = NotificationCompat.Builder(context, EXPENSE_CHANNEL_ID)
                .setSmallIcon(R.drawable.danger)
                .setContentTitle("Expense Alert")
                .setContentText("Monthly expense limit exceeded")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(EXPENSE_NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Timber.e("Error showing expense alert notification: ${e.message}")
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            EXPENSE_CHANNEL_ID,
            "Expense Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createPendingIntent(): PendingIntent {
        // Create an intent to open the app when the notification is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_NAVIGATION_DESTINATION, MainActivity.DESTINATION_ALL_TRANSACTIONS)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    companion object {
        const val EXPENSE_CHANNEL_ID = "expense_alerts"
        const val EXPENSE_NOTIFICATION_ID = 1001
    }
}