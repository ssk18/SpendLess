package com.ssk.auth.presentation.di

import com.ssk.auth.presentation.pinentryscreen.PinEntryViewModel
import com.ssk.auth.presentation.registerscreen.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::PinEntryViewModel)
}