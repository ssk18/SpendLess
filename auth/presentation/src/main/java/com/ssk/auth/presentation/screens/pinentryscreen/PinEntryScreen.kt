package com.ssk.auth.presentation.screens.pinentryscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssk.auth.presentation.R
import com.ssk.auth.presentation.screens.pinentryscreen.PinEntryState.PinEntryMode
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinDots
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinEntry
import com.ssk.core.presentation.designsystem.components.GlobalSnackBar
import com.ssk.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme
import com.ssk.core.presentation.ui.ObserveAsEvents
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun PinEntryScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PinEntryViewModel = koinViewModel<PinEntryViewModel>(),
    onNavigateBack: () -> Unit,
    onNavigateToHome: (userId: Long) -> Unit,
    onNavigateToUserPreferences: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showShakeAnimation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.uiEvents) { events ->
        when (events) {
            PinEntryEvents.OnNavigateBack -> {
                onNavigateBack()
            }

            is PinEntryEvents.OnNavigateToHome -> {
                onNavigateToHome(events.userId)
            }

            PinEntryEvents.ShowPinShakeAnimation -> {
                showShakeAnimation = true
                delay(500)
                showShakeAnimation = false
            }

            is PinEntryEvents.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = events.message.asString(context),
                    duration = SnackbarDuration.Short,
                )
            }

            is PinEntryEvents.NavigateToUserPreferences -> {
                onNavigateToUserPreferences(events.userName, events.pinCode)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            GlobalSnackBar(
                hostState = snackbarHostState,
                snackbarType = state.snackbarType,
            )
        },
        topBar = {
            IconButton(
                onClick = {
                    viewModel.onAction(PinEntryAction.OnBackClick)
                },
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier
    ) {
        PinEntryScreen(
            onAction = viewModel::onAction,
            state = state,
            shoeShakeAnimation = showShakeAnimation,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun PinEntryScreen(
    modifier: Modifier = Modifier,
    state: PinEntryState,
    shoeShakeAnimation: Boolean,
    onAction: (PinEntryAction) -> Unit
) {
    val offset by animateFloatAsState(
        targetValue = if (shoeShakeAnimation) 1f else 0f,
        animationSpec = if (shoeShakeAnimation) {
            keyframes {
                durationMillis = 500
                0f at 0
                -10f at 50
                10f at 150
                -10f at 250
                10f at 350
                0f at 500
            }
        } else {
            spring()
        },
        label = "shake"
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = RegisterIcon,
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (state.mode) {
                PinEntryMode.CREATE -> stringResource(R.string.create_pin)
                PinEntryMode.CONFIRM -> stringResource(R.string.repeat_your_pin)
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (state.mode) {
                PinEntryMode.CREATE -> stringResource(R.string.use_pin_to_login_to_your_account)
                PinEntryMode.CONFIRM -> stringResource(R.string.enter_your_pin_to_login)
            },
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offset.dp.roundToPx(), 0)
                },
            contentAlignment = Alignment.Center
        ) {
            PinDots(
                pinLength = state.pin.length
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        PinEntry(
            onPinClick = {
                onAction(PinEntryAction.OnPinButtonClick(it))
            },
            onDeleteClick = {
                onAction(PinEntryAction.OnDeleteClick)
            },
            onClearPin = {
                onAction(PinEntryAction.OnClearPin)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PinEntryScreenPreview() {
    SpendLessAppTheme {
        PinEntryScreen(
            onAction = {},
            state = PinEntryState(),
            shoeShakeAnimation = true
        )
    }
}