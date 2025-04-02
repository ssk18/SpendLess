package com.ssk.auth.presentation.registerscreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ssk.core.presentation.ui.UiText

@Immutable
data class RegisterState(
    val username: TextFieldState = TextFieldState(),
    val userNameValidationState: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isUsernameTaken: Boolean = false,
    val userNameValidationError: UiText? = null,
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false
)
