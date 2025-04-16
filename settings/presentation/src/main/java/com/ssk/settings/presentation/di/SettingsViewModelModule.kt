package com.ssk.settings.presentation.di

import com.ssk.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsViewModelModule = module {
    viewModelOf(::SettingsViewModel)
}