package com.ssk.auth.presentation.screens.registerscreen

import SpendLessGreen
import SpendLessPurple
import SpendLessRed
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.auth.presentation.R
import com.ssk.core.presentation.designsystem.components.GlobalSnackBar
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessRegisterTextField
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.ArrowForward
import com.ssk.core.presentation.designsystem.theme.CheckIcon
import com.ssk.core.presentation.designsystem.theme.CrossIcon
import com.ssk.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.core.presentation.ui.showTimedSnackBar
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel<RegisterViewModel>(),
    onNextClick: (String) -> Unit,
    onLogInClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle(context = Dispatchers.Main.immediate)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { events ->
        when (events) {
            is RegisterEvent.ShowSnackbar -> {
                scope.showTimedSnackBar(
                    snackbarHostState = snackbarHostState,
                    message = events.message.asString(context)
                )
            }

            is RegisterEvent.UsernameValid -> {
                onNextClick(events.username)
            }

            RegisterEvent.NavigateToLoginScreen -> {
                onLogInClick()
            }
        }
    }

    SpendLessScaffold(
        modifier = modifier,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                GlobalSnackBar(
                    hostState = snackbarHostState,
                    snackbarType = state.snackbarType,
                    modifier = Modifier
                        .fillMaxWidth()
                        .imePadding()
                        .navigationBarsPadding()
                )
            }
        }
    ) {
        RegisterScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier
                .padding(it)
        )
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
            .padding(horizontal = 26.dp)
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
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.create_unique_username),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(36.dp))
        SpendLessRegisterTextField(
            userName = state.username,
            endIcon = if (state.username.text.isNotBlank()) {
                if (state.userNameValidationState) {
                    CheckIcon
                } else {
                    CrossIcon
                }
            } else null,
            endIconTint = if (state.userNameValidationState) {
                SpendLessGreen
            } else {
                SpendLessRed
            },
            hint = stringResource(R.string.username),
            keyboardType = KeyboardType.Text,
        )
        Spacer(modifier = Modifier.height(16.dp))
        SpendLessActionButton(
            text = stringResource(R.string.next),
            enabled = state.isButtonEnabled,
            onClick = {
                onAction(RegisterAction.OnNextClick(state.username.text.toString()))
            },
            icon = ArrowForward
        )
        Spacer(modifier = Modifier.height(30.dp))
        BasicText(
            text = stringResource(R.string.already_have_an_account),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                color = SpendLessPurple,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onAction(RegisterAction.OnAlreadyHaveAnAccountClicked)
                }
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

