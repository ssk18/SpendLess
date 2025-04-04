package com.ssk.auth.presentation.screens.pinentryscreen.components

import SpendLessLightGrey
import SpendLessPurple
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssk.core.presentation.designsystem.theme.SpendLessAppTheme

@Composable
fun PinDots(
    modifier: Modifier = Modifier,
    maxLength: Int = 5,
    pinLength: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(maxLength) { index ->
            val isActive = index < pinLength

            val dotColor by animateColorAsState(
                targetValue = if (isActive) {
                    SpendLessPurple
                } else {
                    SpendLessLightGrey.copy(alpha = 0.12f)
                },
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PinDotsPreview() {
    SpendLessAppTheme {
        PinDots(
            pinLength = 5
        )
    }
}