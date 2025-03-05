package com.ssk.spendless.auth.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class RegisterState(
    val username: String = "",
    val isUsernameValid: Boolean = false,
    val isButtonEnabled: Boolean = false,
    val isUsernameTaken: Boolean = false
)
