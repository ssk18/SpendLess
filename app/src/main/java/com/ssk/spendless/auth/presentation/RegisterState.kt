package com.ssk.spendless.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ssk.spendless.core.presentation.ui.UiText

@Immutable
data class RegisterState(
    val username: TextFieldState = TextFieldState(),
    val userNameValidationState: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isUsernameTaken: Boolean = false,
    val userNameValidationError: UiText? = null
)
