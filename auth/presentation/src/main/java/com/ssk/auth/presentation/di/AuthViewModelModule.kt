package com.ssk.auth.presentation.di

import com.ssk.auth.presentation.screens.login.LoginViewModel
import com.ssk.auth.presentation.screens.pin_prompt.PinPromptViewModel
import com.ssk.auth.presentation.screens.pinentryscreen.PinEntryViewModel
import com.ssk.auth.presentation.screens.registerscreen.RegisterViewModel
import com.ssk.auth.presentation.screens.user_preference.UserPreferencesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::PinEntryViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::UserPreferencesViewModel)
    viewModelOf(::PinPromptViewModel)
}