package com.ssk.core.presentation.designsystem.theme

import SpendLessBlack
import SpendLessDarkGrey
import SpendLessInversePrimary
import SpendLessLightGrey
import SpendLessOnPrimaryContainer
import SpendLessOnPrimaryFixed
import SpendLessOnSecondaryContainer
import SpendLessPaleLavender
import SpendLessPrimaryContainer
import SpendLessPrimaryFixed
import SpendLessPurple
import SpendLessRed
import SpendLessSecondary
import SpendLessSecondaryContainer
import SpendLessSecondaryFixed
import SpendLessSecondaryFixedDim
import SpendLessSuccess
import SpendLessSurfaceContainerLow
import SpendLessSurfaceContainerLowest
import SpendLessTertiaryContainer
import SpendLessWhite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val primaryFixed = SpendLessPrimaryFixed
val onPrimaryFixed = SpendLessOnPrimaryFixed
val secondaryFixed = SpendLessSecondaryFixed
val secondaryFixedDim = SpendLessSecondaryFixedDim
val success = SpendLessSuccess

private val ColorScheme = darkColorScheme(
    primary = SpendLessPurple,
    onPrimary = SpendLessWhite,
    secondary = SpendLessSecondary,
    onSurface = SpendLessBlack,
    onSurfaceVariant = SpendLessDarkGrey,
    error = SpendLessRed,
    background = SpendLessPaleLavender,
    onBackground = SpendLessLightGrey,
    primaryContainer = SpendLessPrimaryContainer,
    onPrimaryContainer = SpendLessOnPrimaryContainer,
    surfaceContainerLowest = SpendLessSurfaceContainerLowest,
    surfaceContainerLow = SpendLessSurfaceContainerLow,
    inversePrimary = SpendLessInversePrimary,
    secondaryContainer = SpendLessSecondaryContainer,
    onSecondaryContainer = SpendLessOnSecondaryContainer,
    tertiaryContainer = SpendLessTertiaryContainer
)

data class StatusBarAppearance(
    val isDarkStatusBarIcons: Boolean = true
)

val LocalStatusBarAppearance = compositionLocalOf { StatusBarAppearance() }

@Composable
fun SpendLessAppTheme(
    content: @Composable () -> Unit
) {
    // Get the controller once
    val systemUiController = rememberSystemUiController()
    
    // Configure system bars once when the theme is applied
    DisposableEffect(systemUiController) {
        // Make both status bar and nav bar transparent with light icons
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false // Light icons for dark background
        )
        
        // Specifically target navigation bar
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = false,
            navigationBarContrastEnforced = false
        )
        
        onDispose {}
    }
    
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}

