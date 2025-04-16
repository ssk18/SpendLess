package com.ssk.settings.presentation.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.components.UserPreferenceFormat
import com.ssk.core.presentation.designsystem.components.dropdown.DropDownSelector
import com.ssk.core.presentation.designsystem.theme.ArrowBack
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.core.presentation.ui.components.DecimalSeparator
import com.ssk.core.presentation.ui.components.SpendLessExpensesFormat
import com.ssk.core.presentation.ui.components.ThousandsSeparator
import com.ssk.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PreferencesScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    navigateUp: () -> Unit,
    navigateToDashboard: () -> Unit
) {
    val state by viewModel.preferencesState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.preferencesEvent) { event ->
        when (event) {
            PreferencesUiEvent.NavigateToDashboard -> {
                navigateToDashboard()
            }
        }
    }

    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "Preferences",
                onStartIconClick = {
                    navigateUp()
                },
                startIcon = ArrowBack,
                titleColor = MaterialTheme.colorScheme.onBackground
            )
        }
    ) {
        PreferencesScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    state: PreferenceUiState,
    onAction: (PreferenceUiAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        UserPreferenceFormat(
            formattedValue = state.expenseFormatState.formattedString
        )
        SpendLessExpensesFormat(
            selectedFormat = state.expenseFormatState.expenseFormat,
            onOptionSelected = {
                onAction(PreferenceUiAction.OnExpenseFormatSelected(it))
            }
        )
        DropDownSelector(
            title = "Currency",
            options = Currency.entries,
            selectedOption = state.expenseFormatState.currency,
            currencyCodeShow = { it.symbol },
            currencyNameShow = { it.title },
            onOptionSelected = {
                onAction(PreferenceUiAction.OnCurrencySelected(it))
            }
        )
        DecimalSeparator(
            selectedDecimal = state.expenseFormatState.decimalSeparatorUi,
            onOptionSelected = {
                onAction(PreferenceUiAction.OnDecimalSeparatorClicked(it))
            }
        )
        ThousandsSeparator(
            selectedThousands = state.expenseFormatState.thousandsSeparatorUi,
            onOptionSelected = {
                onAction(PreferenceUiAction.OnThousandsSeparatorClicked(it))
            }
        )
        SpendLessActionButton(
            text = "Save",
            enabled = state.isSaveButtonEnabled,
            onClick = {
                onAction(PreferenceUiAction.OnSaveClicked)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesScreenPreview() {
    SpendLessAppTheme {
        PreferencesScreen(
            state = PreferenceUiState(),
            onAction = {}
        )
    }
}