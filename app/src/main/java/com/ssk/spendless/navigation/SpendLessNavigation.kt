package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssk.spendless.navigation.graphs.authGraph
import com.ssk.spendless.navigation.graphs.settingsGraph
import com.ssk.spendless.navigation.graphs.transactionsGraph
import com.ssk.spendless.navigation.routes.NavRoute

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavRoute = NavRoute.AuthRoot,
    isUserLoggedIn: Boolean = false
) {
    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) NavRoute.TransactionsRoot else NavRoute.AuthRoot,
        modifier = modifier
    ) {
        authGraph(
            navController,
            modifier
        )
        transactionsGraph(
            navController = navController,
            modifier = modifier
        )
        settingsGraph(
            navController = navController,
            modifier = modifier
        )
    }
}

