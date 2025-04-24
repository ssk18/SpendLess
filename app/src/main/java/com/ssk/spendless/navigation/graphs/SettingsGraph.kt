package com.ssk.spendless.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.settings.presentation.SettingsViewModel
import com.ssk.settings.presentation.preferences.PreferencesScreenRoot
import com.ssk.settings.presentation.security.SecurityScreenRoot
import com.ssk.settings.presentation.settings.SettingsScreenRoot
import com.ssk.spendless.navigation.routes.NavRoute
import com.ssk.spendless.navigation.sharedViewModel

fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.SettingsRoot>(
        startDestination = NavRoute.Settings
    ) {
        composable<NavRoute.Settings> { entry ->
            val settingsSharedViewModel = entry.sharedViewModel<SettingsViewModel>(navController)

            SettingsScreenRoot(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToLogin = {
                    // Navigate to Login and clear backstack
                    navController.navigate(NavRoute.Login) {
                        // Clear the entire back stack - important after logout
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                navigateToPreferences = {
                    navController.navigate(NavRoute.Preferences)
                },
                navigateToSecurity = {
                    navController.navigate(NavRoute.Security)
                },
                navigateToPinPrompt = {
                  navController.navigate(NavRoute.PinPrompt)
                },
                viewModel = settingsSharedViewModel,
                modifier = modifier
            )
        }

        composable<NavRoute.Preferences> { entry ->
            val settingsSharedViewModel = entry.sharedViewModel<SettingsViewModel>(navController)
            PreferencesScreenRoot(
                modifier = modifier,
                viewModel = settingsSharedViewModel,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToDashboard = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavRoute.Security> { entry ->
            val settingsSharedViewModel = entry.sharedViewModel<SettingsViewModel>(navController)
            SecurityScreenRoot(
                modifier = modifier,
                viewModel = settingsSharedViewModel,
                navigateUp = {
                    navController.navigateUp()
                },
                navigateToPinPrompt = {
                    navController.navigate(NavRoute.PinPrompt) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}