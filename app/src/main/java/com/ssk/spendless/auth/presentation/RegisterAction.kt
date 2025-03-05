package com.ssk.spendless.auth.presentation

interface RegisterAction {
    data object OnNextClick: RegisterAction
    data object OnUserNameChange: RegisterAction
}