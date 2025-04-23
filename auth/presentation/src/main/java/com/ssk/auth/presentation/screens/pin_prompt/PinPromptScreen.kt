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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinDots
import com.ssk.auth.presentation.screens.pinentryscreen.components.PinEntry
import com.ssk.core.presentation.designsystem.components.AppTopBar
import com.ssk.core.presentation.designsystem.components.SpendLessScaffold
import com.ssk.core.presentation.designsystem.theme.ExitIcon
import com.ssk.core.presentation.designsystem.theme.RegisterIcon

@Composable
fun PinPromptScreenRoot(
    modifier: Modifier = Modifier,
) {

}

@Composable
fun PinPromptScreen(
    modifier: Modifier = Modifier,
    state: PinPromptUiState,
    shoeShakeAnimation: Boolean,
    onAction: (PinPromptUiAction) -> Unit
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
    SpendLessScaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                endIcon2 = ExitIcon,
                endIcon2Color = MaterialTheme.colorScheme.error,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                onEndIcon2Click = {

                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = modifier
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
                text = "Hello, ${state.username}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your PIN",
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
                    pinLength = state.pinCode.length
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
                onClearPin = {

                }
            )
        }
    }
}