package com.ssk.auth.presentation.screens.pinentryscreen

import com.ssk.core.presentation.designsystem.components.SnackbarType
import com.ssk.core.presentation.ui.UiText

sealed interface PinEntryEvents {
    data class ShowSnackbar(
        val message: UiText,
        val type: SnackbarType = SnackbarType.Info
    ) : PinEntryEvents
    data object OnNavigateBack: PinEntryEvents
    data class OnNavigateToHome(val userId: Long) : PinEntryEvents
    data object ShowPinShakeAnimation : PinEntryEvents
    data class NavigateToUserPreferences(val userName: String, val pinCode: String) : PinEntryEvents
}