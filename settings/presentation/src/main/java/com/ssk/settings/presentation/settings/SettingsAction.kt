package com.ssk.settings.presentation.settings

sealed interface SettingsAction {
    data object OnPreferencesClicked : SettingsAction
    data object OnSecurityClicked : SettingsAction
    data object OnLogOutClicked : SettingsAction
}