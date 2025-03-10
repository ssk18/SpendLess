package com.ssk.spendless.auth.presentation.registerscreen

import com.ssk.spendless.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent

}