package com.ssk.dashboard.presentation.di

import com.ssk.dashboard.presentation.create_transaction.CreateTransactionViewModel
import com.ssk.dashboard.presentation.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    viewModelOf(::DashboardViewModel)
    
    // Provide dependencies explicitly for CreateTransactionViewModel
    viewModel { 
        CreateTransactionViewModel(
            userRepository = get()
        ) 
    }
}