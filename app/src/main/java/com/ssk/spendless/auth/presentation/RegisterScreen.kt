package com.ssk.spendless.auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.spendless.R
import com.ssk.spendless.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.spendless.core.presentation.designsystem.components.SpendLessTextField
import com.ssk.spendless.core.presentation.designsystem.theme.CheckIcon
import com.ssk.spendless.core.presentation.designsystem.theme.CrossIcon
import com.ssk.spendless.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessDarkRed
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessPurple
import com.ssk.spendless.core.presentation.designsystem.theme.SpendLessWhite
import com.ssk.spendless.core.presentation.designsystem.theme.displayFontFamily
import kotlinx.coroutines.Dispatchers

@Composable
fun RegisterScreenRoot(
    viewModel: RegisterViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RegisterScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
            modifier = modifier
        )
        SnackbarHost(
            hostState = viewModel.snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                snackbarData = it
            )
        }
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterState = RegisterState(),
    onAction: (RegisterAction) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(20.dp)
            .padding(top = 68.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = RegisterIcon,
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.welcome_to_spendless),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.create_unique_username),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(20.dp))
        SpendLessTextField(
            userName = state.username,
            hint = stringResource(R.string.username),
            keyboardType = KeyboardType.Text,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SpendLessActionButton(
            text = stringResource(R.string.next),
            enabled = state.isButtonEnabled,
            onClick = {
                onAction(RegisterAction.OnNextClick)
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        BasicText(
            text = stringResource(R.string.already_have_an_account),
            style = TextStyle(
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Light,
                color = SpendLessPurple,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onAction(RegisterAction.OnNextClick)
                }
        )
    }
}

@Composable
fun UserNameRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isValid) {
                CheckIcon
            } else {
                CrossIcon
            },
            contentDescription = null,
            tint = if (isValid) SpendLessPurple else SpendLessDarkRed
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SpendLessAppTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
        )
    }
}

