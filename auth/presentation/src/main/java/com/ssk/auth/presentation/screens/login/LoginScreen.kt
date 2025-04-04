package com.ssk.auth.presentation.screens.login

import SpendLessPurple
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.auth.presentation.R
import com.ssk.core.presentation.designsystem.components.GlobalSnackBar
import com.ssk.core.presentation.designsystem.components.SpendLessActionButton
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.components.SpendLessTextField
import com.ssk.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.core.presentation.ui.showTimedSnackBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
    onLogInClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { events ->
        when (events) {
            is LoginEvents.ShowSnackbar -> {
                scope.showTimedSnackBar(
                    snackbarHostState = snackbarHostState,
                    message = events.message.asString(context)
                )
            }

            LoginEvents.NavigateToRegisterScreen -> {
                onRegisterClick()
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
        LoginScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier
                .padding(it)
        )
    }

}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit
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
            text = stringResource(R.string.welcome_back),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.enter_your_login_details),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(36.dp))
        SpendLessTextField(
            userName = state.username,
            hint = stringResource(R.string.username),
            keyboardType = KeyboardType.Text,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SpendLessTextField(
            userName = state.pin,
            hint = stringResource(R.string.pin),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SpendLessActionButton(
            text = stringResource(R.string.log_in),
            onClick = {
                onAction(LoginAction.OnLoginClick)
            },
        )
        Spacer(modifier = Modifier.height(30.dp))
        BasicText(
            text = stringResource(R.string.new_to_spendless),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                color = SpendLessPurple,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onAction(LoginAction.OnRegisterClick)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SpendLessAppTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}