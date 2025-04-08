package com.ssk.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SpendLessScaffold(
    modifier: Modifier = Modifier,
    applyGradient: Boolean = false,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
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