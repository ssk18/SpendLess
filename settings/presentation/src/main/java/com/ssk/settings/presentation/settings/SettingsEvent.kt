package com.ssk.settings.presentation.settings

sealed interface SettingsEvent {
    data object NavigateToLogin : SettingsEvent
    data object NavigateBack: SettingsEvent
}