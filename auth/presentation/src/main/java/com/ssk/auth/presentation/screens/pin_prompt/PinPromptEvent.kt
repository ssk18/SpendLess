package com.ssk.auth.presentation.screens.pin_prompt

import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText

sealed interface PinPromptEvent {
    data class ShowSnackbar(
        val message: UiText,
        val type: SnackbarType = SnackbarType.Info
    ) : PinPromptEvent
    data object NavigateToLogin: PinPromptEvent
    data object OnSuccessfulPin : PinPromptEvent
}