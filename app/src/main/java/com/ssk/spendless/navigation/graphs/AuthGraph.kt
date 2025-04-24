package com.ssk.spendless.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.auth.presentation.screens.login.LoginScreenRoot
import com.ssk.auth.presentation.screens.pin_prompt.PinPromptScreenRoot
import com.ssk.auth.presentation.screens.pinentryscreen.PinEntryScreenRoot
import com.ssk.auth.presentation.screens.registerscreen.RegisterScreenRoot
import com.ssk.auth.presentation.screens.user_preference.OnboardingPreferenceScreenRoot
import com.ssk.spendless.navigation.routes.NavRoute

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    isUserSessionExpired: Boolean,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.AuthRoot>(
        startDestination = if (isUserSessionExpired) NavRoute.PinPrompt else NavRoute.Register
    ) {
        composable<NavRoute.Register> {
            RegisterScreenRoot(
                onNextClick = {
                    navController.navigate(NavRoute.PinEntry(it))
                },
                onLogInClick = {
                    navController.navigate(NavRoute.Login)
                },
                modifier = modifier
            )
        }

        composable<NavRoute.PinEntry> {
            PinEntryScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(NavRoute.Register)
                },
                onNavigateToUserPreferences = { username, pinCode ->
                    navController.navigate(NavRoute.UserPreferences(username, pinCode))
                },
                modifier = modifier
            )
        }

        composable<NavRoute.Login> {
            LoginScreenRoot(
                onDashboardClick = {
                    navController.navigate(NavRoute.Dashboard)
                },
                onRegisterClick = {
                    navController.navigate(NavRoute.Register)
                }
            )
        }

        composable<NavRoute.UserPreferences> {
            OnboardingPreferenceScreenRoot(
                onStartButtonClicked = {
                    navController.navigate(NavRoute.Dashboard)
                },
                onBackClicked = {
                    navController.navigate(NavRoute.PinEntry(it.toString()))
                }
            )
        }

        composable<NavRoute.PinPrompt> {
            PinPromptScreenRoot(
                navigateToLogin = {
                    navController.navigate(NavRoute.Login)
                },
                navigateBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(NavRoute.Dashboard) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    }
}