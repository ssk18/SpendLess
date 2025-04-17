package com.ssk.core.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Controls status bar appearance for any screen.
 * Will automatically reset to the default appearance when the composable leaves composition.
 *
 * @param darkIcons If true, status bar icons will be dark (black); if false, they'll be light (white)
 * @param color Background color of the status bar
 */
@Composable
fun StatusBarEffect(darkIcons: Boolean, color: Color = Color.Transparent) {
    val systemUiController = rememberSystemUiController()
    
    DisposableEffect(systemUiController, darkIcons, color) {
        Log.d("StatusBarEffect", "Setting status bar: darkIcons=$darkIcons, color=$color")
        
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = darkIcons
        )
        
        onDispose {
            // Reset to default (light icons on dark background)
            Log.d("StatusBarEffect", "Resetting status bar to default")
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = false
            )
        }
    }
}

/**
 * Specifically for bottom sheets and dialogs that need to manage their own status bar appearance.
 * Note: This may not work with Material3 ModalBottomSheet. Use StatusBarEffect instead.
 *
 * @param isDarkStatusBarIcons Set to true for dark status bar icons (black), false for light icons (white)
 */
@Composable
fun UpdateDialogStatusBarAppearance(isDarkStatusBarIcons: Boolean) {
    val view = LocalView.current

    Log.d(
        "StatusBarDebug",
        "UpdateDialogStatusBarAppearance called with isDarkStatusBarIcons=$isDarkStatusBarIcons"
    )
    Log.d(
        "StatusBarDebug",
        "View class: ${view.javaClass.simpleName}, parent: ${view.parent?.javaClass?.simpleName}"
    )

    val dialogWindow = remember {
        (view.parent as? DialogWindowProvider)?.window
    }

    if (dialogWindow != null) {
        DisposableEffect(isDarkStatusBarIcons) {
            Log.d(
                "StatusBarDebug",
                "DisposableEffect triggered for dialogWindow with isDarkStatusBarIcons=$isDarkStatusBarIcons"
            )
            val controller = WindowCompat.getInsetsController(dialogWindow, view)
            val originalState = controller.isAppearanceLightStatusBars

            Log.d(
                "StatusBarDebug",
                "Original status bar state: isAppearanceLightStatusBars=$originalState"
            )
            controller.isAppearanceLightStatusBars = isDarkStatusBarIcons
            Log.d(
                "StatusBarDebug",
                "Updated status bar to: isAppearanceLightStatusBars=$isDarkStatusBarIcons"
            )

            onDispose {
                Log.d(
                    "StatusBarDebug",
                    "DisposableEffect disposing, resetting status bar to original state: $originalState"
                )
                controller.isAppearanceLightStatusBars = originalState
            }
        }
    } else {
        Log.d("StatusBarDebug", "No DialogWindow found, using SystemUiController instead")
        // Use SystemUiController as fallback
        StatusBarEffect(darkIcons = isDarkStatusBarIcons)
    }
}