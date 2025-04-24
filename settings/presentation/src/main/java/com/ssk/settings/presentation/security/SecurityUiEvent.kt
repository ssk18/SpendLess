package com.ssk.settings.presentation.security

sealed interface SecurityUiEvent {
    data object NavigateToPinPrompt: SecurityUiEvent
    data object NavigateBack: SecurityUiEvent
}