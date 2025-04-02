package com.ssk.auth.presentation.pinentryscreen

import com.ssk.core.presentation.ui.UiText

data class PinEntryState(
    val mode: PinEntryMode = PinEntryMode.CREATE,
    val pin: String = "",
    val isPinComplete: Boolean = false,
    val initialPin: String = "",
    val maxPinLength: Int = 5,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

enum class PinEntryMode {
    CREATE,
    CONFIRM
}