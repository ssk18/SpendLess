package com.ssk.settings.presentation.security

import com.ssk.settings.presentation.security.components.BiometricsPromptUi
import com.ssk.settings.presentation.security.components.LockedOutDurationUi
import com.ssk.settings.presentation.security.components.SessionExpiryDurationUi

data class SecurityUiState(
    val selectedBiometricsPrompt: BiometricsPromptUi = BiometricsPromptUi.DISABLE,
    val selectedSessionExpiryDuration: SessionExpiryDurationUi = SessionExpiryDurationUi.FIVE_MIN,
    val lockedOutDuration: LockedOutDurationUi = LockedOutDurationUi.ONE_MIN,
)
