package com.ssk.auth.presentation.screens.pin_prompt

import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.presentation.designsystem.components.SnackbarType

data class PinPromptUiState(
    val username: String = "",
    val pinCode: String = "",
    val remainingPinAttempts: Int = 3,
    val isExceededFailedAttempts: Boolean = false,
    val lockOutTimeRemaining: Long = 0L,
    val snackbarType: SnackbarType = SnackbarType.Error,
    val lockoutDuration: LockedOutDuration = LockedOutDuration.FIFTEEN_SEC,
)
