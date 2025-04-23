package com.ssk.auth.presentation.screens.pin_prompt

import com.ssk.auth.presentation.R
import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText

data class PinPromptUiState(
    val username: String = "",
    val description: UiText = UiText.StringResource(R.string.enter_you_pin),
    val pinCode: String = "",
    val currentAttempt: Int = 0,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val snackbarType: SnackbarType = SnackbarType.Info,
    val isKeyboardLocked: Boolean = false,
)
