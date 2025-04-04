package com.ssk.auth.presentation.screens.registerscreen

import com.ssk.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data class ShowSnackbar(val message: UiText) : RegisterEvent
    data class UsernameValid(val username: String) : RegisterEvent
    data object NavigateToLoginScreen : RegisterEvent
}