package com.ssk.settings.presentation.security.components

import androidx.compose.runtime.Composable
import com.ssk.core.presentation.designsystem.components.SegmentOption
import com.ssk.core.presentation.ui.components.OptionText

enum class BiometricsPromptUi(override val label: @Composable () -> Unit) : SegmentOption {
    ENABLE(
        label = {
            OptionText(text = "Enable")
        }
    ),
    DISABLE(
        label = {
            OptionText(text = "Disable")
        }
    )
}

enum class SessionExpiryDurationUi(override val label: @Composable (() -> Unit)) : SegmentOption {
    FIVE_MIN(label = { OptionText(text = "5 min") }),
    FIFTEEN_MIN(label = { OptionText(text = "15 min") }),
    THIRTY_MIN(label = { OptionText(text = "30 min") }),
    ONE_HOUR(label = { OptionText(text = "1 hour") })
}

enum class LockedOutDurationUi(override val label: @Composable (() -> Unit)) : SegmentOption {
    FIFTEEN_SEC(label = { OptionText(text = "15s") }),
    THIRTY_SEC(label = { OptionText(text = "30s") }),
    ONE_MIN(label = { OptionText(text = "1 min") }),
    FIVE_MIN(label = { OptionText(text = "5 min") })
}