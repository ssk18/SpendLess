package com.ssk.auth.presentation.screens.user_preference

sealed interface UserPreferenceEvent {
    data object OnBackClicked : UserPreferenceEvent
    data object NavigateToDashboardScreen : UserPreferenceEvent
}