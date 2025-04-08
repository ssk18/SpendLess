package com.ssk.auth.presentation.screens.user_preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.auth.presentation.R
import com.ssk.auth.presentation.screens.user_preference.components.BackButton
import com.ssk.auth.presentation.screens.user_preference.components.UserPreferenceFormat
import com.ssk.core.domain.model.Currency
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector
import com.ssk.core.presentation.designsystem.components.dropdown.DropDownSelector
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.core.presentation.ui.components.DecimalSeparatorUi
import com.ssk.core.presentation.ui.components.ExpensesFormatUi
import com.ssk.core.presentation.ui.components.SettingItem
import com.ssk.core.presentation.ui.components.ThousandsSeparatorUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingPreferenceScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: UserPreferencesViewModel = koinViewModel(),
    onStartButtonClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvents) { event ->
        when (event) {
            UserPreferenceEvent.NavigateToDashboardScreen -> {
                onStartButtonClicked()
            }
            UserPreferenceEvent.OnBackClicked -> {
                onBackClicked()
            }
        }
    }

    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            IconButton(
                onClick = {
                    viewModel.onAction(UserPreferenceAction.OnBackClicked)
                },
                modifier = Modifier
            ) {

            }
        }
    ) {
        OnboardingPreferenceScreenContent(
            modifier = Modifier
                .padding(it),
            state = state ,
            onAction = viewModel::onAction
        )
    }

}

@Composable
fun OnboardingPreferenceScreenContent(
    modifier: Modifier = Modifier,
    state: UserPreferenceState,
    onAction: (UserPreferenceAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(
            onClick = {
                onAction(UserPreferenceAction.OnBackClicked)
            }
        )
        Text(
            text = stringResource(R.string.set_spendless_to_your_preferences),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "You can change it at any time in Settings",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        UserPreferenceFormat(
            formattedValue = state.expensesFormatState.formattedString
        )

        Spacer(modifier = Modifier.height(20.dp))

        ExpensesFormat(
            selectedFormat = state.expensesFormatState.expenseFormat,
            onOptionSelected = {
                onAction(UserPreferenceAction.OnExpenseFormatUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropDownSelector(
            title = "Currency",
            options = Currency.entries,
            selectedOption = state.expensesFormatState.currency,
            currencyCodeShow = { it.symbol },
            currencyNameShow = { it.title },
            onOptionSelected = {
                onAction(UserPreferenceAction.OnCurrencyUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DecimalSeparator(
            selectedDecimal = state.expensesFormatState.decimalSeparatorUi,
            onOptionSelected = {
                println("Selected Decimal Separator: $it")
                onAction(UserPreferenceAction.OnDecimalSeparatorUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThousandsSeparator(
            selectedThousands = state.expensesFormatState.thousandsSeparatorUi,
            onOptionSelected = {
                onAction(UserPreferenceAction.OnThousandsSeparatorUpdate(it))
            }
        )

        Spacer(modifier = Modifier.height(34.dp))

        SpendLessActionButton(
            text = "Start Tracking!",
            enabled = state.isStartButtonEnabled,
            onClick = {
                onAction(UserPreferenceAction.OnStartClicked)
            }
        )
    }
}

@Composable
fun DecimalSeparator(
    modifier: Modifier = Modifier,
    selectedDecimal: DecimalSeparatorUi,
    onOptionSelected: (DecimalSeparatorUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Decimal Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = DecimalSeparatorUi.entries,
            selectedOption = selectedDecimal,
            onOptionSelected = {

                onOptionSelected(it as DecimalSeparatorUi)
            }
        )
    }
}

@Composable
fun ThousandsSeparator(
    modifier: Modifier = Modifier,
    selectedThousands: ThousandsSeparatorUi,
    onOptionSelected: (ThousandsSeparatorUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Thousands Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = ThousandsSeparatorUi.entries,
            selectedOption = selectedThousands,
            onOptionSelected = {
                onOptionSelected(it as ThousandsSeparatorUi)
            }
        )
    }
}

@Composable
fun ExpensesFormat(
    modifier: Modifier = Modifier,
    selectedFormat: ExpensesFormatUi,
    onOptionSelected: (ExpensesFormatUi) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Expense Format"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = ExpensesFormatUi.entries,
            selectedOption = selectedFormat,
            onOptionSelected = {
                onOptionSelected(it as ExpensesFormatUi)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreferenceScreenPreview() {
    SpendLessAppTheme {
        OnboardingPreferenceScreenContent(
            state = UserPreferenceState(),
            onAction = {}
        )
    }
}