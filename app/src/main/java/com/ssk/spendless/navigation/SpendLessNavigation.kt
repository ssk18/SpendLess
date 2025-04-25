package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.spendless.navigation.graphs.authGraph
import com.ssk.spendless.navigation.graphs.navigateToPinPrompt
import com.ssk.spendless.navigation.graphs.settingsGraph
import com.ssk.spendless.navigation.graphs.transactionsGraph
import com.ssk.spendless.navigation.routes.NavRoute

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavRoute,
    sessionRepository: ISessionRepository
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isUserSessionExpired = sessionRepository.isUserLoggedIn() && sessionRepository.isSessionExpired()

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START && isUserSessionExpired) {
                navController.navigateToPinPrompt()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authGraph(
            navController,
            isUserSessionExpired,
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

