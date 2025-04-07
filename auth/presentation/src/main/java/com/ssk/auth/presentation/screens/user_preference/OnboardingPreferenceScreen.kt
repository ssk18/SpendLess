package com.ssk.auth.presentation.screens.user_preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.auth.presentation.R
import com.ssk.auth.presentation.screens.user_preference.components.UserPreferenceFormat
import com.ssk.core.presentation.designsystem.components.SpendLessSegmentSelector
import com.ssk.core.presentation.designsystem.components.dropdown.DropDownSelector
import com.ssk.core.presentation.designsystem.components.dropdown.FakeCurrency
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.components.DecimalSeparator
import com.ssk.core.presentation.ui.components.SettingItem
import com.ssk.core.presentation.ui.components.ThousandsSeparator

@Composable
fun OnboardingPreferenceScreenRoot(
    modifier: Modifier = Modifier,
) {
    OnboardingPreferenceScreenContent(
        modifier = modifier,
        state = UserPreferenceState()
    )
}

@Composable
fun OnboardingPreferenceScreenContent(
    modifier: Modifier = Modifier,
    state: UserPreferenceState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        UserPreferenceFormat()

        Spacer(modifier = Modifier.height(20.dp))

        DropDownSelector(
            title = "Currency",
            options = FakeCurrency.entries,
            selectedOption = FakeCurrency.INR,
            currencyCodeShow = { it.symbol },
            currencyNameShow = { it.title },
            onOptionSelected = {  }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DecimalSeparator(
            selectedDecimal = state.expensesFormatState.decimalSeparator,
            onOptionSelected = {  }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThousandsSeparator(
            selectedThousands = state.expensesFormatState.thousandsSeparator,
            onOptionSelected = {  }
        )

    }
}

@Composable
fun DecimalSeparator(
    modifier: Modifier = Modifier,
    selectedDecimal: DecimalSeparator,
    onOptionSelected: (DecimalSeparator) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Decimal Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = DecimalSeparator.entries,
            selectedOption = selectedDecimal,
            onOptionSelected = {
                onOptionSelected(it as DecimalSeparator)
            }
        )
    }
}

@Composable
fun ThousandsSeparator(
    modifier: Modifier = Modifier,
    selectedThousands: ThousandsSeparator,
    onOptionSelected: (ThousandsSeparator) -> Unit,
) {
    SettingItem(
        modifier = modifier,
        title = "Thousands Separator"
    ) {
        SpendLessSegmentSelector(
            segmentOptions = DecimalSeparator.entries,
            selectedOption = selectedThousands,
            onOptionSelected = {
                onOptionSelected(it as ThousandsSeparator)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreferenceScreenPreview() {
    SpendLessAppTheme {
        OnboardingPreferenceScreenContent(
            state = UserPreferenceState()
        )
    }
}