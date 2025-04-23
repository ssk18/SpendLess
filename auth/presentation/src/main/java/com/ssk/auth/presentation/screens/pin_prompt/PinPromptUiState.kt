package com.ssk.auth.presentation.screens.pin_prompt

import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText

data class PinPromptUiState(
    val username: String = "",
    val pinCode: String = "",
    val isPinComplete: Boolean = false,
    val initialPin: String = "",
    val maxPinLength: Int = 5,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val snackbarType: SnackbarType = SnackbarType.Info
)
