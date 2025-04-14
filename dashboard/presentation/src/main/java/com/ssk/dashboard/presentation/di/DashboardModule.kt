package com.ssk.dashboard.presentation.di

import com.ssk.dashboard.presentation.TransactionSharedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::TransactionSharedViewModel)
}