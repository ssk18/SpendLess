package com.ssk.settings.presentation.preferences

sealed interface PreferencesUiEvent {
    data object NavigateToDashboard : PreferencesUiEvent
}