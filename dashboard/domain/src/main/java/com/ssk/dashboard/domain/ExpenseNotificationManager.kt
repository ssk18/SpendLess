package com.ssk.dashboard.domain

interface ExpenseNotificationManager {
    /**
     * Shows an expense alert notification
     * @param thresholdAmount The expense threshold amount that was exceeded
     */
    fun showExpenseAlert(thresholdAmount: Float)
}