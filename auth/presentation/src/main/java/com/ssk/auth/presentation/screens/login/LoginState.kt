package com.ssk.auth.presentation.screens.login

import androidx.compose.foundation.text.input.TextFieldState
import com.ssk.core.presentation.designsystem.components.SnackbarType

data class LoginState(
    val username: TextFieldState = TextFieldState(),
    val pin: TextFieldState = TextFieldState(),
    val snackbarType: SnackbarType = SnackbarType.Info
)
