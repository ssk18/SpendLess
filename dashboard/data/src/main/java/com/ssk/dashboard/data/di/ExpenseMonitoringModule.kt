package com.ssk.dashboard.data.di

import com.ssk.dashboard.data.expense_alarm.ExpenseAlarmSchedulerImpl
import com.ssk.dashboard.data.expense_alarm.ExpenseThresholdMonitorImpl
import com.ssk.dashboard.domain.ExpenseAlarmScheduler
import com.ssk.dashboard.domain.ExpenseThresholdMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val expenseMonitoringModule = module {
    single<ExpenseAlarmScheduler> { ExpenseAlarmSchedulerImpl(androidContext()) }
    single<ExpenseThresholdMonitor> { ExpenseThresholdMonitorImpl(get()) }
}