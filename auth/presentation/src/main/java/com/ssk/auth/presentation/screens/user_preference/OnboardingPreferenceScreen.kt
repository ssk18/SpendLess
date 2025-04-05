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
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun OnboardingPreferenceScreenRoot(
    modifier: Modifier = Modifier,
) {
    OnboardingPreferenceScreen(modifier = modifier)
}

@Composable
fun OnboardingPreferenceScreen(
    modifier: Modifier = Modifier,
) {
    OnboardingPreferenceScreenContent(
        modifier = modifier
    )
}

@Composable
fun OnboardingPreferenceScreenContent(
    modifier: Modifier = Modifier,
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


    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreferenceScreenPreview() {
    SpendLessAppTheme {
        OnboardingPreferenceScreen()
    }
}