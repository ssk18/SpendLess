package com.ssk.spendless.auth.presentation.registerscreen

import com.ssk.spendless.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data class Error(val error: UiText): RegisterEvent
    data class ShowSnackbar(val message: UiText): RegisterEvent
    data class UsernameValid(val username: String) : RegisterEvent
}