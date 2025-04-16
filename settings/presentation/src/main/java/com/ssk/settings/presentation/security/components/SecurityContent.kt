package com.ssk.settings.presentation.security.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.components.SettingItem
import com.ssk.settings.presentation.security.SecurityUiAction
import com.ssk.settings.presentation.security.SecurityUiState

@Composable
fun SecurityContent(
    modifier: Modifier = Modifier,
    state: SecurityUiState,
    onAction: (SecurityUiAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BiometricsPromptSelection(
            selectedOption = state.selectedBiometricsPrompt,
            onOptionSelected = { onAction(SecurityUiAction.OnBiometricsPromptChanged(it)) }
        )
        SessionExpiryDurationSelection(
            selectedOption = state.selectedSessionExpiryDuration,
            onOptionSelected = { onAction(SecurityUiAction.OnSessionExpiryDurationChanged(it)) }
        )
        LockedOutDurationSelection(
            selectedOption = state.lockedOutDuration,
            onOptionSelected = { onAction(SecurityUiAction.OnLockedOutDurationChanged(it)) }
        )
    }
}

@Composable
fun BiometricsPromptSelection(
    modifier: Modifier = Modifier,
    selectedOption: BiometricsPromptUi,
    onOptionSelected: (BiometricsPromptUi) -> Unit
) {
    SettingItem(
        modifier = modifier,
        title = "Biometrics for PIN prompt"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = BiometricsPromptUi.entries,
            selectedOption = selectedOption,
            onOptionSelected = { onOptionSelected(it as BiometricsPromptUi) }
        )
    }
}

@Composable
fun SessionExpiryDurationSelection(
    modifier: Modifier = Modifier,
    selectedOption: SessionExpiryDurationUi,
    onOptionSelected: (SessionExpiryDurationUi) -> Unit
) {
    SettingItem(
        modifier = modifier,
        title = "Session expiry duration"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = SessionExpiryDurationUi.entries,
            selectedOption = selectedOption,
            onOptionSelected = { onOptionSelected(it as SessionExpiryDurationUi) }
        )
    }
}

@Composable
fun LockedOutDurationSelection(
    modifier: Modifier = Modifier,
    selectedOption: LockedOutDurationUi,
    onOptionSelected: (LockedOutDurationUi) -> Unit
) {
    SettingItem(
        modifier = modifier,
        title = "Locked out duration"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = LockedOutDurationUi.entries,
            selectedOption = selectedOption,
            onOptionSelected = { onOptionSelected(it as LockedOutDurationUi) }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SecurityContentPreview() {
    SpendLessAppTheme {
        SecurityContent(
            state = SecurityUiState(),
            onAction = {}
        )
    }
}
