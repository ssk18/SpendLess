package com.ssk.spendless.auth.presentation.registerscreen

interface RegisterAction {
    data object OnNextClick: RegisterAction
    data class OnUserNameChange(val username: String): RegisterAction
}