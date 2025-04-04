package com.ssk.core.presentation.designsystem.components

import SpendLessGreen
import SpendLessRed
import SpendLessSecondaryFixed
import SpendLessWhite
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GlobalSnackBar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    snackbarType: SnackbarType,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data ->
            SpendLessEventBanner(
                text = data.visuals.message,
                snackbarType = snackbarType
            )
        }
    )
}

@Composable
fun SpendLessEventBanner(
    modifier: Modifier = Modifier,
    text: String,
    snackbarType: SnackbarType
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .addBackgroundColor(snackbarType)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = addBackgroundContentColor(snackbarType),
            textAlign = TextAlign.Center
        )
    }
}

enum class SnackbarType {
    Success,
    Error,
    Info
}

@Composable
fun Modifier.addBackgroundColor(snackbarType: SnackbarType): Modifier {
    return this.then(
        when (snackbarType) {
            SnackbarType.Success -> Modifier.background(SpendLessGreen)
            SnackbarType.Error -> Modifier.background(SpendLessRed)
            SnackbarType.Info -> Modifier.background(SpendLessSecondaryFixed)
        }
    )
}

@Composable
fun BoxScope.addBackgroundContentColor(snackbarType: SnackbarType): Color {
    return when (snackbarType) {
        SnackbarType.Success -> SpendLessWhite
        SnackbarType.Error -> SpendLessWhite
        SnackbarType.Info -> SpendLessWhite
    }
}