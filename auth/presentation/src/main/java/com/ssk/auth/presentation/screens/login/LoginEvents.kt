package com.ssk.auth.presentation.screens.login

import com.ssk.core.presentation.ui.UiText

sealed interface LoginEvents {
    data class ShowSnackbar(
        val message: UiText
    ): LoginEvents
    data object NavigateToRegisterScreen: LoginEvents
}