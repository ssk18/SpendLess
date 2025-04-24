package com.ssk.auth.presentation.screens.pin_prompt

sealed interface PinPromptUiAction {
    data object OnLogoutClick : PinPromptUiAction
    data class OnPinButtonClick(val pin: String) : PinPromptUiAction
    data object OnDeleteClick : PinPromptUiAction
    data object VerifyPinLockStatus: PinPromptUiAction
}