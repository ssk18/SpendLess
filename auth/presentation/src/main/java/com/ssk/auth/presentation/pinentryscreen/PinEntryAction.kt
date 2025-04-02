package com.ssk.auth.presentation.pinentryscreen

sealed interface PinEntryAction {
    data object OnBackClick : PinEntryAction
    data class OnPinButtonClick(val pin: String) : PinEntryAction
    data object OnDeleteClick : PinEntryAction
    data object OnClearPin: PinEntryAction
    data object OnSubmitPin: PinEntryAction
}