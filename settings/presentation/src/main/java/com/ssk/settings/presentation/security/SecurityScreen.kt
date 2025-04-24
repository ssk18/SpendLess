package com.ssk.settings.presentation.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.ArrowBack
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.settings.presentation.SettingsViewModel
import com.ssk.settings.presentation.security.components.SecurityContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun SecurityScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    navigateToPinPrompt: () -> Unit
) {
    val state by viewModel.securityState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.securityEvent) { event ->
        when (event) {
            is SecurityUiEvent.NavigateBack -> navigateUp()
            is SecurityUiEvent.NavigateToPinPrompt -> navigateToPinPrompt()
        }
    }

    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "Security",
                startIcon = ArrowBack,
                onStartIconClick = {
                    navigateUp()
                },
                titleColor = MaterialTheme.colorScheme.onBackground
            )
        }
    ) {
        SecurityScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun SecurityScreen(
    modifier: Modifier = Modifier,
    state: SecurityUiState,
    onAction: (SecurityUiAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SecurityContent(
            state = state,
            onAction = onAction
        )
        SpendLessActionButton(
            text = "Save",
            onClick = { onAction(SecurityUiAction.OnSaveClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityScreenPreview() {
    SpendLessAppTheme {
        SecurityScreen(
            state = SecurityUiState(),
            onAction = {}
        )
    }
}