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
                onBackClick = {
                    navController.navigateUp()
                },
                navigateToLogin = {
                    navController.navigate(NavRoute.Login)
                },
                navigateToPreferences = {
                    navController.navigate(NavRoute.Preferences)
                },
                navigateToSecurity = {
                    navController.navigate(NavRoute.Security)
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
                }
            )
        }
    }
}