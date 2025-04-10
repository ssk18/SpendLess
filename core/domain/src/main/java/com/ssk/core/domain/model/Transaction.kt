package com.ssk.core.domain.model

import java.time.Instant
import java.time.Year
import java.time.ZoneId

data class Transaction(
    val id: Long = 0L,
    val userId: Long,
    val title: String,
    val amount: Float,
    val repeatType: RepeatType,
    val transactionType: TransactionType,
    val note: String? = null,
    val transactionDate: Long = Instant.now().toEpochMilli()
) {
    fun shouldRepeat(currentTimeMillis: Long): Boolean {
        val transactionDate = Instant.ofEpochMilli(this.transactionDate)
        val currentDate = Instant.ofEpochMilli(currentTimeMillis)

        return when (repeatType) {
            RepeatType.NOT_REPEAT -> false
            RepeatType.DAILY -> true
            RepeatType.WEEKLY -> isWeeklyRepeat(transactionDate, currentDate)
            RepeatType.MONTHLY -> isMonthlyRepeat(transactionDate, currentDate)
            RepeatType.YEARLY -> isYearlyRepeat(transactionDate, currentDate)
        }
    }

    private fun isWeeklyRepeat(transactionDate: Instant, currentDate: Instant): Boolean {
        val dayOfWeek = transactionDate.atZone(ZoneId.systemDefault()).dayOfWeek
        val currentDayOfWeek = currentDate.atZone(ZoneId.systemDefault()).dayOfWeek
        return dayOfWeek == currentDayOfWeek
    }

    private fun isMonthlyRepeat(transactionDate: Instant, currentDate: Instant): Boolean {
        val transactionDayOfMonth = transactionDate.atZone(ZoneId.systemDefault()).dayOfMonth
        val currentDayOfMonth = currentDate.atZone(ZoneId.systemDefault()).dayOfMonth

        if (transactionDayOfMonth == 31) {
            val lastDayOfCurrentMonth = currentDate.atZone(ZoneId.systemDefault()).withDayOfMonth(
                currentDate.atZone(ZoneId.systemDefault()).month.length(
                    Year.from(currentDate.atZone(ZoneId.systemDefault())).isLeap
                )
            ).dayOfMonth
            return lastDayOfCurrentMonth == currentDayOfMonth
        }

        return transactionDayOfMonth == currentDayOfMonth
    }

    private fun isYearlyRepeat(transactionDate: Instant, currentDate: Instant): Boolean {
        val transactionMonth = transactionDate.atZone(ZoneId.systemDefault()).monthValue
        val transactionDayOfYear = transactionDate.atZone(ZoneId.systemDefault()).dayOfYear
        val currentMonth = currentDate.atZone(ZoneId.systemDefault()).monthValue
        val currentDayOfYear = currentDate.atZone(ZoneId.systemDefault()).dayOfYear

        // Check if the transaction date is February 29th
        if (transactionMonth == 2 && transactionDayOfYear == 60) {
            // Check if the current year is a leap year
            val isLeapYear = Year.isLeap(currentDate.atZone(ZoneId.systemDefault()).year.toLong())

            // If the year is not a leap year, then check the last day of February
            if (!isLeapYear) {
                // Check if today is the last day of February (28th day in a non-leap year)
                val lastDayOfFebruary = 59
                return currentDayOfYear == lastDayOfFebruary
            }
        }

        return transactionMonth == currentMonth && transactionDayOfYear == currentDayOfYear
    }
}

sealed interface TransactionType

enum class Expense(val type: String) : TransactionType {
    HOME("Home"),
    FOOD("Food & Groceries"),
    ENTERTAINMENT("Entertainment"),
    CLOTHING("Clothing & Accessories"),
    HEALTH("Health & Wellness"),
    PERSONAL_CARE("Personal Care"),
    TRANSPORTATION("Transportation"),
    EDUCATION("Education"),
    SAVING("Saving & Investments"),
    OTHER("Other")
}

data object Income : TransactionType

enum class RepeatType {
    NOT_REPEAT,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}