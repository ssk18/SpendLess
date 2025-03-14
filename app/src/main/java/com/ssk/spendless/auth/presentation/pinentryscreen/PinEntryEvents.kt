package com.ssk.spendless.auth.presentation.pinentryscreen

import com.ssk.spendless.core.presentation.ui.UiText

sealed interface PinEntryEvents {
    data class ShowSnackbar(
        val message: UiText,
        val type: SnackbarType = SnackbarType.Info
    ) : PinEntryEvents
    data object OnNavigateBack: PinEntryEvents
    data class OnNavigateToHome(val userId: Long) : PinEntryEvents
    data object ShowPinShakeAnimation : PinEntryEvents
}

enum class SnackbarType {
    Success,
    Error,
    Info
}