package com.ssk.spendless.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.auth.presentation.screens.login.LoginScreenRoot
import com.ssk.auth.presentation.screens.pinentryscreen.PinEntryScreenRoot
import com.ssk.auth.presentation.screens.registerscreen.RegisterScreenRoot
import com.ssk.spendless.navigation.routes.NavRoute

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.AuthRoot>(
        startDestination = NavRoute.Register
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
                modifier = modifier
            )
        }

        composable<NavRoute.Login> {
            LoginScreenRoot(
                onLogInClick = {},
                onRegisterClick = {
                    navController.navigate(NavRoute.Register)
                }
            )
        }
    }
}