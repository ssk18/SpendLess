package com.ssk.settings.presentation.preferences

sealed interface PreferenceUiEvent {
    data object NavigateUp : PreferenceUiEvent
    data object NavigateToPinPrompt: PreferenceUiEvent
}