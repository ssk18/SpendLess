package com.ssk.settings.presentation.security.components

import com.ssk.core.domain.model.BiometricsPrompt
import com.ssk.core.domain.model.LockedOutDuration
import com.ssk.core.domain.model.SessionExpiryDuration

fun BiometricsPrompt.toUi(): BiometricsPromptUi {
    return when (this) {
        BiometricsPrompt.ENABLE -> BiometricsPromptUi.ENABLE
        BiometricsPrompt.DISABLE -> BiometricsPromptUi.DISABLE
    }
}

fun SessionExpiryDuration.toUi(): SessionExpiryDurationUi {
    return when (this) {
        SessionExpiryDuration.FIVE_MIN -> SessionExpiryDurationUi.FIVE_MIN
        SessionExpiryDuration.FIFTEEN_MIN -> SessionExpiryDurationUi.FIFTEEN_MIN
        SessionExpiryDuration.THIRTY_MIN -> SessionExpiryDurationUi.THIRTY_MIN
        SessionExpiryDuration.HOUR -> SessionExpiryDurationUi.ONE_HOUR
    }
}

fun LockedOutDuration.toUi(): LockedOutDurationUi {
    return when (this) {
        LockedOutDuration.FIFTEEN_SEC -> LockedOutDurationUi.FIFTEEN_SEC
        LockedOutDuration.THIRTY_SEC -> LockedOutDurationUi.THIRTY_SEC
        LockedOutDuration.ONE_MIN -> LockedOutDurationUi.ONE_MIN
        LockedOutDuration.FIVE_MIN -> LockedOutDurationUi.FIVE_MIN
    }
}

fun BiometricsPromptUi.toDomain(): BiometricsPrompt {
    return when (this) {
        BiometricsPromptUi.ENABLE -> BiometricsPrompt.ENABLE
        BiometricsPromptUi.DISABLE -> BiometricsPrompt.DISABLE
    }
}

fun LockedOutDurationUi.toDomain(): LockedOutDuration {
    return when (this) {
        LockedOutDurationUi.FIFTEEN_SEC -> LockedOutDuration.FIFTEEN_SEC
        LockedOutDurationUi.THIRTY_SEC -> LockedOutDuration.THIRTY_SEC
        LockedOutDurationUi.ONE_MIN -> LockedOutDuration.ONE_MIN
        LockedOutDurationUi.FIVE_MIN -> LockedOutDuration.FIVE_MIN
    }
}

fun SessionExpiryDurationUi.toDomain(): SessionExpiryDuration {
    return when (this) {
        SessionExpiryDurationUi.FIVE_MIN -> SessionExpiryDuration.FIVE_MIN
        SessionExpiryDurationUi.FIFTEEN_MIN -> SessionExpiryDuration.FIFTEEN_MIN
        SessionExpiryDurationUi.THIRTY_MIN -> SessionExpiryDuration.THIRTY_MIN
        SessionExpiryDurationUi.ONE_HOUR -> SessionExpiryDuration.HOUR
    }
}