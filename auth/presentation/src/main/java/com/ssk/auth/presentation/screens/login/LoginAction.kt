package com.ssk.auth.presentation.screens.login

sealed interface LoginAction {
    data class OnUserNameChange(val userName: String): LoginAction
    data class OnPinChange(val pin: String): LoginAction
    data object OnLoginClick: LoginAction
    data object OnRegisterClick: LoginAction
}