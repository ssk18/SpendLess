package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.spendless.auth.presentation.pinentryscreen.PinEntryScreenRoot
import com.ssk.spendless.auth.presentation.registerscreen.RegisterScreenRoot

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.HomeRoot,
        modifier = modifier
    ) {
        homeRootGraph(
            navController,
            modifier
        )
    }
}

private fun NavGraphBuilder.homeRootGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.HomeRoot>(
        startDestination = NavRoute.Register
    ) {
        composable<NavRoute.Register> {
            RegisterScreenRoot(
                onNextClick = {
                    navController.navigate(NavRoute.PinEntry(it))
                },
                modifier = modifier
            )
        }

        composable<NavRoute.PinEntry> {
            PinEntryScreenRoot(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToHome = {
                    navController.navigate(NavRoute.Register)
                },
                modifier = modifier
            )
        }
    }
}