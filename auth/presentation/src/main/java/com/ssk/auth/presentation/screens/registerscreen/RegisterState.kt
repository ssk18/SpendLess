package com.ssk.auth.presentation.screens.registerscreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import com.ssk.core.presentation.designsystem.components.SnackbarType

@Immutable
data class RegisterState(
    val username: TextFieldState = TextFieldState(),
    val isButtonEnabled: Boolean = false,
    val snackbarType: SnackbarType = SnackbarType.Info,
    val userNameValidationState: Boolean = false
)
