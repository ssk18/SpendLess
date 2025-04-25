package com.ssk.auth.presentation.screens.pin_prompt

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ssk.auth.presentation.R
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinDots
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinEntry
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.GlobalSnackBar
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.ExitIcon
import com.ssk.core.presentation.designsystem.theme.RegisterIcon
import com.ssk.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun PinPromptScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PinPromptViewModel = koinViewModel(),
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit

) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showShakeAnimation by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.onAction(PinPromptUiAction.VerifyPinLockStatus)
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            PinPromptEvent.NavigateToLogin -> {
                navigateToLogin()
            }

            PinPromptEvent.OnSuccessfulPin -> {
                navigateBack()
            }
            is PinPromptEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    PinPromptScreen(
        modifier = modifier,
        state = viewModel.state,
        showShakeAnimation = showShakeAnimation,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )

}

@Composable
fun PinPromptScreen(
    modifier: Modifier = Modifier,
    state: PinPromptUiState,
    showShakeAnimation: Boolean,
    snackbarHostState: SnackbarHostState,
    onAction: (PinPromptUiAction) -> Unit
) {
    val offset by animateFloatAsState(
        targetValue = if (showShakeAnimation) 1f else 0f,
        animationSpec = if (showShakeAnimation) {
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
    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                endIcon2 = ExitIcon,
                endIcon2Color = MaterialTheme.colorScheme.error,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                onEndIcon2Click = {
                    onAction(PinPromptUiAction.OnLogoutClick)
                }
            )
        },
        snackbarHost = {
            GlobalSnackBar(
                hostState = snackbarHostState,
                snackbarType = state.snackbarType
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = RegisterIcon,
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (state.currentAttempt > 3) {
                LockedPinPromptText(lockoutTime = state.)
            } else {
                Text(
                    text = stringResource(R.string.pin_prompt_sub_headline),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(offset.dp.roundToPx(), 0)
                    },
                contentAlignment = Alignment.Center
            ) {
                PinDots(
                    pinLength = state.pinCode.length,
                    isLocked = state.isKeyboardLocked
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            PinEntry(
                onPinClick = {
                    onAction(PinPromptUiAction.OnPinButtonClick(it))
                },
                onDeleteClick = {
                    onAction(PinPromptUiAction.OnDeleteClick)
                },
                isLocked = state.isKeyboardLocked
            )
        }
    }
}

@Composable
private fun LockedPinPromptText(lockoutTime: Long) {
    val textStyle = MaterialTheme.typography.bodyMedium
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = textStyle.fontFamily,
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append(stringResource(R.string.try_your_pin_again_in))
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = textStyle.fontFamily,
                    color = textStyle.color,
                    fontSize = textStyle.fontSize,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" ")
                append(lockoutTime.)
            }
        }
    )
}