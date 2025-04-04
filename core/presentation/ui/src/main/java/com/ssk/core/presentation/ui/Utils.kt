package com.ssk.core.presentation.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.showTimedSnackBar(
    snackbarHostState: SnackbarHostState,
    durationMillis: Long = 2000L,
    message: String
) {
    launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        val snackBarJob = launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
        }
        delay(durationMillis)
        snackBarJob.cancel()
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}