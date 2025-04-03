package com.ssk.auth.presentation.screens.registerscreen

interface RegisterAction {
    data class OnNextClick(val username: String): RegisterAction
    data class OnUserNameChange(val username: String): RegisterAction
    data object OnLoginCLick: RegisterAction
}