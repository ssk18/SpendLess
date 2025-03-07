package com.ssk.spendless.auth.presentation

interface RegisterAction {
    data object OnNextClick: RegisterAction
    data class OnUserNameChange(val username: String): RegisterAction
}