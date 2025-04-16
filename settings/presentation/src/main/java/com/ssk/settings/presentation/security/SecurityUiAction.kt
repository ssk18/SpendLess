package com.ssk.settings.presentation.security

import com.ssk.settings.presentation.security.components.BiometricsPromptUi
import com.ssk.settings.presentation.security.components.LockedOutDurationUi
import com.ssk.settings.presentation.security.components.SessionExpiryDurationUi

sealed interface SecurityUiAction {
    data class OnBiometricsPromptChanged(val biometricsPromptUi: BiometricsPromptUi) :
        SecurityUiAction

    data class OnSessionExpiryDurationChanged(val sessionExpiryDurationUi: SessionExpiryDurationUi) :
        SecurityUiAction

    data class OnLockedOutDurationChanged(val lockedOutDurationUi: LockedOutDurationUi) :
        SecurityUiAction

    data object OnSaveClicked : SecurityUiAction
}