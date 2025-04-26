package com.ssk.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssk.core.domain.repository.ISessionRepository
import com.ssk.core.presentation.ui.ObserveAsEvents
import com.ssk.spendless.MainEvent
import com.ssk.spendless.MainViewModel
import com.ssk.spendless.navigation.graphs.authGraph
import com.ssk.spendless.navigation.graphs.settingsGraph
import com.ssk.spendless.navigation.graphs.transactionsGraph
import com.ssk.spendless.navigation.routes.NavRoute
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpendLessNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavRoute,
    sessionRepository: ISessionRepository
) {
    val isUserSessionExpired = remember {
        sessionRepository.isUserLoggedIn() && sessionRepository.isSessionExpired()
    }
    val mainViewModel = koinViewModel<MainViewModel>()

    ObserveAsEvents(mainViewModel.event) {  event ->
        when (event) {
            MainEvent.SessionExpired -> {
                navController.navigate(NavRoute.PinPrompt)
            }
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

