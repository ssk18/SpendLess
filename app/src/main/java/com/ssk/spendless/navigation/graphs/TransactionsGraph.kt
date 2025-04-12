package com.ssk.spendless.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.dashboard.presentation.dashboard.DashboardScreenRoot
import com.ssk.spendless.navigation.routes.NavRoute

fun NavGraphBuilder.transactionsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.TransactionsRoot>(
        startDestination = NavRoute.Dashboard
    ) {
        composable<NavRoute.Dashboard> {
            DashboardScreenRoot()
        }
    }
}