package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarData
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
    snackbarColor: @Composable (SnackbarData) -> Color,
    contentColor: Color
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { data ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(snackbarColor(data))
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = data.visuals.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}