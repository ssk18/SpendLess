package com.ssk.auth.presentation.screens.pinentryscreen

import androidx.compose.runtime.Immutable
import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText

@Immutable
data class PinEntryState(
    val mode: PinEntryMode = PinEntryMode.CREATE,
    val pin: String = "",
    val isPinComplete: Boolean = false,
    val initialPin: String = "",
    val maxPinLength: Int = 5,
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val snackbarType: SnackbarType = SnackbarType.Info
) {
    sealed interface PinEntryMode {
        data object CREATE: PinEntryMode
        data object CONFIRM: PinEntryMode
    }
}