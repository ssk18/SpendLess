package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssk.spendless.navigation.graphs.authGraph
import com.ssk.spendless.navigation.routes.NavRoute

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.AuthRoot,
        modifier = modifier
    ) {
        authGraph(
            navController,
            modifier
        )
    }
}

