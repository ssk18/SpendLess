package com.ssk.core.presentation.designsystem.components

import android.app.Activity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.ssk.core.presentation.designsystem.theme.LocalStatusBarAppearance

@Composable
fun SpendLessScaffold(
    modifier: Modifier = Modifier,
    applyGradient: Boolean = false,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val view = LocalView.current
    val statusBarAppearance = LocalStatusBarAppearance.current

    if (!view.isInEditMode) {
        LaunchedEffect(view) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                statusBarAppearance.isDarkStatusBarIcons
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = bottomBar,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
    ) { paddingValues ->
        if (applyGradient) {
            SpendLessGradientBackground {
                content(paddingValues)
            }
        } else {
            content(paddingValues)
        }
    }
}