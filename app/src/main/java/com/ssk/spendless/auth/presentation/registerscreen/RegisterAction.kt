package com.ssk.spendless.auth.presentation.registerscreen

interface RegisterAction {
    data class OnNextClick(val username: String): RegisterAction
    data class OnUserNameChange(val username: String): RegisterAction
}