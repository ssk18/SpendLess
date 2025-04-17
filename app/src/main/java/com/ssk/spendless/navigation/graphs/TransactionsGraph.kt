package com.ssk.spendless.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ssk.dashboard.presentation.TransactionSharedViewModel
import com.ssk.dashboard.presentation.all_transactions.AllTransactionsScreenRoot
import com.ssk.dashboard.presentation.dashboard.DashboardScreenRoot
import com.ssk.spendless.navigation.routes.NavRoute
import com.ssk.spendless.navigation.sharedViewModel

fun NavGraphBuilder.transactionsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation<NavRoute.TransactionsRoot>(
        startDestination = NavRoute.Dashboard
    ) {
        composable<NavRoute.Dashboard> { entry ->
            val transactionSharedViewModel =
                entry.sharedViewModel<TransactionSharedViewModel>(navController)
            DashboardScreenRoot(
                navigateToSettings = {
                    navController.navigate(NavRoute.Settings)
                },
                navigateToAllTransactions = {
                    navController.navigate(NavRoute.AllTransactions)
                },
                viewModel = transactionSharedViewModel,
                modifier = modifier
            )
        }

        composable<NavRoute.AllTransactions> { entry ->
            val transactionSharedViewModel =
                entry.sharedViewModel<TransactionSharedViewModel>(navController)
            AllTransactionsScreenRoot(
                viewModel = transactionSharedViewModel,
                onBackClick = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}